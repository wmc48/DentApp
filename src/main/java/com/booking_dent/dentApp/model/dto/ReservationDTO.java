package com.booking_dent.dentApp.model.dto;

import com.booking_dent.dentApp.database.entity.EmployeeEntity;
import com.booking_dent.dentApp.database.entity.PatientEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO{

private Long visitId;
private Long employeeId;
private Long patientId;
private String peselPatient;
private LocalDateTime dateTime;
private boolean confirmed;
}
