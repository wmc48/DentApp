package com.booking_dent.dentApp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

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

    // Metoda mapująca
//    public static ScheduleDTO fromEntity(ScheduleEntity entity) {
//        ScheduleDTO dto = new ScheduleDTO();
//        dto.setScheduleId(Long.valueOf(entity.getScheduleId()));
//        dto.setEmployeeId(Long.valueOf(entity.getEmployee().getEmployeeId()));
//        dto.setShiftId(Long.valueOf(entity.getShift().getShiftId()));
//        dto.setWorkDate(entity.getWorkDate());
//        dto.setEmployeeName(entity.getEmployee().getName() + " " + entity.getEmployee().getSurname());
//        dto.setShiftName(entity.getShift().getName());
//        dto.setShiftStartTime(entity.getShift().getStartTime());
//        dto.setShiftEndTime(entity.getShift().getEndTime());
//        return dto;
//    }
}

