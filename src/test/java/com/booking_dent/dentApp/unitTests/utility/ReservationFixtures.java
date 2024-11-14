package com.booking_dent.dentApp.unitTests.utility;

import com.booking_dent.dentApp.database.entity.ReservationEntity;
import com.booking_dent.dentApp.database.entity.ShiftEntity;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.LocalTime;

@UtilityClass
public class ReservationFixtures {

    public static ReservationEntity testReservation1() {
        return ReservationEntity.builder()
                .reservationId(1L)
                .employee(EmployeeFixtures.testEmployee1())
                .patient(PatientFixtures.createTestPatient1())
                .dateAndTime(LocalDateTime.of(2024, 5, 15, 10, 0))
                .confirmed(false)
                .build();
    }

    public static ReservationEntity testReservation2() {
        return ReservationEntity.builder()
                .reservationId(2L)
                .employee(EmployeeFixtures.testEmployee2())
                .patient(PatientFixtures.createTestPatient2())
                .dateAndTime(LocalDateTime.of(2024, 5, 15, 14, 30))
                .confirmed(true)
                .build();
    }
}
