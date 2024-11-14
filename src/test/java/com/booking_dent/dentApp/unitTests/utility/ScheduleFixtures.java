package com.booking_dent.dentApp.unitTests.utility;
import com.booking_dent.dentApp.model.dto.ScheduleDTO;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;

@UtilityClass
public class ScheduleFixtures {
    public static ScheduleDTO testSchedule1() {
        return ScheduleDTO.builder()
                .scheduleId(1L)
                .employeeId(123L)
                .shiftId(2L)
                .workDate(LocalDate.of(2024, 11, 1))
                .workDateFrom(LocalDate.of(2024, 11, 1))
                .workDateTo(LocalDate.of(2024, 11, 1))
                .employeeName("Jan")
                .employeeSurname("Dto")
                .shiftName("Morning")
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(18, 0))
                .availableHours(Arrays.asList("12:00", "10:00", "11:00"))
                .build();
    }
    public static ScheduleDTO testSchedule2() {
        return ScheduleDTO.builder()
                .scheduleId(2L)
                .employeeId(123L)
                .shiftId(2L)
                .workDate(LocalDate.of(2024, 11, 2))
                .workDateFrom(LocalDate.of(2024, 11, 2))
                .workDateTo(LocalDate.of(2024, 11, 2))
                .employeeName("Jan")
                .employeeSurname("Dto")
                .shiftName("Morning")
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(18, 0))
                .availableHours(Arrays.asList("10:00", "11:00"))
                .build();
    }
}
