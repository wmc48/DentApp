package com.booking_dent.dentApp.unitTests.utility;

import com.booking_dent.dentApp.model.dto.MonthDetails;

import java.time.LocalDate;

public class MonthFixtures {
    public static MonthDetails testMonthDetails() {
        return MonthDetails.builder()
                .currentMonth(LocalDate.of(2024, 2, 1))
                .previousMonth(LocalDate.of(2024, 1, 1))
                .nextMonth(LocalDate.of(2024, 3, 1))
                .currentMonthName("Luty 2024")
                .build();
    }
}
