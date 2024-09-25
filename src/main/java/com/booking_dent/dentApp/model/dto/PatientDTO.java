package com.booking_dent.dentApp.model.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientDTO {

    private Long patientId;
    private String name;
    private String surname;
    private String pesel;
    private String email;
    private String phone;

}
