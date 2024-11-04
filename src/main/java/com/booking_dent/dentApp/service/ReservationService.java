package com.booking_dent.dentApp.service;

import com.booking_dent.dentApp.database.entity.EmployeeEntity;
import com.booking_dent.dentApp.database.entity.PatientEntity;
import com.booking_dent.dentApp.database.entity.ReservationEntity;
import com.booking_dent.dentApp.database.repository.EmployeeRepository;
import com.booking_dent.dentApp.database.repository.PatientRepository;
import com.booking_dent.dentApp.database.repository.ReservationRepository;
import com.booking_dent.dentApp.model.dto.ReservationDTO;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ReservationService {

    private ReservationRepository reservationRepository; // repozytorium do rezerwacji
    private EmployeeRepository employeeRepository; // repozytorium do pracowników
    private PatientRepository patientRepository; // repozytorium do pacjentów

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

    public void addReservation(ReservationDTO reservationDTO) {
        //pobieranie encji EmployeeEntity i PatientEntity na podstawie ich ID
        EmployeeEntity employee = employeeRepository.findById(reservationDTO.getEmployeeId())
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono pracownika o ID: " + reservationDTO.getEmployeeId()));
        PatientEntity patient = patientRepository.findById(reservationDTO.getPatientId())
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono pacjenta o ID: " + reservationDTO.getPatientId()));


        ReservationEntity reservation = ReservationEntity.builder()
                .employee(employee)
                .patient(patient)
                .dateAndTime(reservationDTO.getDateAndTime())
                .confirmed(false)
                .build();

        reservationRepository.save(reservation);
    }
}





