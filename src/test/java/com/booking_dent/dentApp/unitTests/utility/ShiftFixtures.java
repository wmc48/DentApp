package com.booking_dent.dentApp.unitTests.utility;

import com.booking_dent.dentApp.database.entity.ShiftEntity;
import lombok.experimental.UtilityClass;

import java.time.LocalTime;

@UtilityClass
public class ShiftFixtures {

    //shifts
    public static ShiftEntity testShift1() {
        return ShiftEntity.builder()
                .shiftId(0L)
                .name("morning")
                .startTime(LocalTime.of(8, 0, 0))
                .endTime(LocalTime.of(16, 0, 0))
                .build();
    }

    public static ShiftEntity testShift2() {
        return ShiftEntity.builder()
                .shiftId(0L)
                .name("afternoon")
                .startTime(LocalTime.of(14, 0, 0))
                .endTime(LocalTime.of(22, 0, 0))
                .build();
    }
}
