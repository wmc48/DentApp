package com.booking_dent.dentApp.service;

import com.booking_dent.dentApp.database.entity.*;
import com.booking_dent.dentApp.database.repository.*;
import com.booking_dent.dentApp.model.dto.ReservationDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final PatientRepository patientRepository;
    private final EmployeeRepository employeeRepository;
    private final ScheduleRepository scheduleRepository;


//    public ReservationEntity addReservation(ReservationDTO reservationDTO){
//        ReservationEntity newReservation = ReservationEntity.builder()
//                .employeeId(reservationDTO.getEmployeeId())
//                .patientId(reservationDTO.getPatientId())
//                .dateTime(reservationDTO.getDateTime())
//                .build();
//
////        private Long visitId;
////        private Long employeeId;
////        private Long patientId;
////        private String peselPatient;
////        private LocalDateTime dateTime;
////        private boolean confirmed;
//    }





}
