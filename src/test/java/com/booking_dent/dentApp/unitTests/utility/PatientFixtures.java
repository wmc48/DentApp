package com.booking_dent.dentApp.unitTests.utility;

import com.booking_dent.dentApp.database.entity.PatientEntity;
import com.booking_dent.dentApp.model.dto.PatientDTO;
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

    public static PatientDTO testPatientDto() {
        return PatientDTO.builder()
                .name("Jan")
                .surname("Dto")
                .email("jan.dto@gmail.com")
                .phone("123456789")
                .pesel("12378901")
                .build();
    }
}
