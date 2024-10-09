package com.booking_dent.dentApp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO {

    private Long reservationId;
    private LocalDateTime dateAndTime;
    //    private LocalDateTime dateFrom;
//    private LocalDateTime dateTo;
    private String selectedHour;
    private boolean confirmed;

    private Long employeeId;
    private String employeeName;
    private String employeeSurname;

    private Long patientId;
    private String patientName;
    private String patientSurname;
    private String patientPesel;
}
