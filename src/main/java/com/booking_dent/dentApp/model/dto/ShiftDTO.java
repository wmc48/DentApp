package com.booking_dent.dentApp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShiftDTO {

    private Long shiftId;
    private Long employeeId;
    private String name;
    private LocalTime startTime;
    private LocalTime endTime;

}
