package com.booking_dent.dentApp.service;

import com.booking_dent.dentApp.database.entity.EmployeeEntity;
import com.booking_dent.dentApp.database.entity.PatientEntity;
import com.booking_dent.dentApp.database.entity.ReservationEntity;
import com.booking_dent.dentApp.database.repository.EmployeeRepository;
import com.booking_dent.dentApp.database.repository.PatientRepository;
import com.booking_dent.dentApp.database.repository.ReservationRepository;
import com.booking_dent.dentApp.model.dto.ReservationDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ReservationService {

    private ReservationRepository reservationRepository; // Repozytorium do rezerwacji
    private EmployeeRepository employeeRepository; // `Repozytorium do pracowników
    private PatientRepository patientRepository; // Repozytorium do pacjentów

    public List<ReservationEntity> getAllReservations() {
        return reservationRepository.findAll();
    }

    public void deleteById(Long reservationId) {
        reservationRepository.deleteById(reservationId);
    }

    public List<ReservationEntity> findReservation(ReservationDTO reservationDTO) {
        return reservationRepository.findReservation(
                reservationDTO.getPatientName(),
                reservationDTO.getPatientSurname(),
                reservationDTO.getPatientPesel(),

                reservationDTO.getEmployeeName(),
                reservationDTO.getEmployeeSurname(),
                reservationDTO.getEmployeeId(),

//                reservationDTO.getDateAndTime(),
//                reservationDTO.getDateAndTime(),

                reservationDTO.getReservationId()

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





