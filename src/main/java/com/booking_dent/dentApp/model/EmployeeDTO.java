package com.booking_dent.dentApp.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {

    private Integer employeeId;
    private String name;
    private String surname;
    private String email;
    private String phone;

}
