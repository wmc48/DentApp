package com.booking_dent.dentApp.unitTests.utility;

import com.booking_dent.dentApp.database.entity.EmployeeEntity;
import com.booking_dent.dentApp.model.dto.EmployeeDTO;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EmployeeFixtures {
    public static EmployeeEntity testEmployee1(){
        return EmployeeEntity.builder()
                .employeeId(1L)
                .name("Test1")
                .surname("TestoweNazw1")
                .email("email@email.com")
                .phone("8888888888")
                .build();
    }

    public static EmployeeEntity testEmployee2(){
        return EmployeeEntity.builder()
                .employeeId(2L)
                .name("Test2")
                .surname("TestoweNazw2")
                .email("test@email.com")
                .phone("9999999999")
                .build();
    }

    public static EmployeeDTO testEmployeeDto() {
        return EmployeeDTO.builder()
                .name("Test1")
                .surname("TestoweNazw1")
                .email("email@email.com")
                .phone("8888888888")
                .build();
    }
}
