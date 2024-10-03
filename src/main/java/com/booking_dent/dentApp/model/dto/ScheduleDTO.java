package com.booking_dent.dentApp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleDTO {

    private Long scheduleId;
    private Long employeeId;
    private Long shiftId;
    private LocalDate workDate;

    private String employeeName;
    private String employeeSurname;
    private String shiftName;
    private LocalTime startTime;
    private LocalTime endTime;
    private List<String> availableHours;


}

