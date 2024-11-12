package com.booking_dent.dentApp.unitTests;

import com.booking_dent.dentApp.database.entity.EmployeeEntity;
import com.booking_dent.dentApp.database.entity.ScheduleEntity;
import com.booking_dent.dentApp.database.entity.ShiftEntity;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.LocalTime;

@UtilityClass
public class EntityFixtures {
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

    //shifts
    public static ShiftEntity testShift1(){
        return ShiftEntity.builder()
                .shiftId(0L)
                .name("morning")
                .startTime(LocalTime.of(8, 0, 0))
                .endTime(LocalTime.of(16,0, 0))
                .build();
    }
    public static ShiftEntity testShift2(){
        return ShiftEntity.builder()
                .shiftId(0L)
                .name("afternoon")
                .startTime(LocalTime.of(14,0, 0))
                .endTime(LocalTime.of(22,0, 0))
                .build();
    }

    //schedules
    public static ScheduleEntity testSchedule1(){
        return ScheduleEntity.builder()
                .scheduleId(0L)
                .employee(testEmployee1())
                .shift(testShift1())
                .workDate(LocalDate.of(2024,10,6))
                .build();
    }
}
