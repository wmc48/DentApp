package com.booking_dent.dentApp.service;

import com.booking_dent.dentApp.database.entity.EmployeeEntity;
import com.booking_dent.dentApp.database.entity.PatientEntity;
import com.booking_dent.dentApp.database.entity.ReservationEntity;
import com.booking_dent.dentApp.database.repository.EmployeeRepository;
import com.booking_dent.dentApp.database.repository.PatientRepository;
import com.booking_dent.dentApp.database.repository.ReservationRepository;
import com.booking_dent.dentApp.model.dto.ReservationDTO;
import com.booking_dent.dentApp.model.mapper.ReservationMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class ReservationService {

    private ReservationRepository reservationRepository; // repozytorium do rezerwacji
    private EmployeeRepository employeeRepository; // repozytorium do pracowników
    private PatientRepository patientRepository; // repozytorium do pacjentów
    private final ReservationMapper reservationMapper;

    public List<ReservationDTO> getDTOReservationsForEmployee(Long employeeId) {
        return reservationRepository.findByEmployee_EmployeeId(employeeId).stream()
                .map(reservationMapper::toDTO)
                .toList();
    }

    public List<ReservationEntity> getAllReservations() {
        return reservationRepository.findAll();
    }

    public Page<ReservationEntity> getReservations(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return reservationRepository.findAll(pageable);
    }

    public void deleteById(Long reservationId) {
        reservationRepository.deleteById(reservationId);
    }

    public Page<ReservationEntity> findReservation(ReservationDTO reservationDTO, Pageable pageable) {
        return reservationRepository.findReservation(
                reservationDTO.getPatientName(),
                reservationDTO.getPatientSurname(),
                reservationDTO.getPatientPesel(),
                reservationDTO.getEmployeeName(),
                reservationDTO.getEmployeeSurname(),
                reservationDTO.getEmployeeId(),
                reservationDTO.getReservationId(),
                pageable // pageable do repozytorium
        );
    }

    public void addReservation(String workDate, String selectedHour, Long employeeId, Long patientId) {
        List<ReservationEntity> futureReservations = reservationRepository.findFutureReservationsByPatient(
                patientId,
                LocalDateTime.now()
        );
        //bład jeśliużytkownik próbuje dodać wizyte chociaż od date.now ma już dodana
        //czyli uż może mieć zaplanowaną w przó tylko jedną wizyte
        if (!futureReservations.isEmpty()) {
            throw new IllegalStateException("Pacjent ma już zaplanowaną wizytę");
        }

        //połączenie daty i godziny
        String dateTimeString = workDate + "T" + selectedHour; // tworzenie formatu ISO 8601

        LocalDateTime dateAndTime = LocalDateTime.parse(dateTimeString);
        EmployeeEntity employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono pracownika o ID: " + employeeId));
        PatientEntity patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono pacjenta o ID: " + patientId));

        ReservationEntity reservation = ReservationEntity.builder()
                .employee(employee)
                .patient(patient)
                .dateAndTime(dateAndTime)
                .confirmed(false)
                .build();

        reservationRepository.save(reservation);
    }
}





