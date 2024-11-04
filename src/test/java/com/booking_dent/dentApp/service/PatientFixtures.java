package com.booking_dent.dentApp.service;

import com.booking_dent.dentApp.database.entity.PatientEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PatientFixtures {
    public static PatientEntity createTestPatient1() {
        return PatientEntity.builder()
                .patientId(1L)
                .name("Jan")
                .surname("Kowalski")
                .pesel("12345678901")
                .email("jan.kowalski@gmail.com")
                .phone("123456789")
                .build();
    }
    public static PatientEntity createTestPatient2() {
        return PatientEntity.builder()
                .patientId(2L)
                .name("Adam")
                .surname("Testowy")
                .pesel("999999999999")
                .email("ad.test@gmail.com")
                .phone("544548")
                .build();
    }
}
