package com.booking_dent.dentApp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
//dla wyświetlania widoku danego miesiąca
public class MonthDetails {
    private LocalDate currentMonth;
    private LocalDate previousMonth;
    private LocalDate nextMonth;
    private String currentMonthName;
}
