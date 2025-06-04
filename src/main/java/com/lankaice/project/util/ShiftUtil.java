package com.lankaice.project.util;

import com.lankaice.project.dto.ShiftInfoDto;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class ShiftUtil {


    public static ShiftInfoDto getShiftBasedOnTime() {
        LocalDateTime now = LocalDateTime.now();
        LocalTime currentTime = now.toLocalTime();

        LocalTime morningStart = LocalTime.of(8, 0);
        LocalTime morningEnd = LocalTime.of(17, 0);

        String date;
        String shift;

        if (!currentTime.isBefore(morningStart) && currentTime.isBefore(morningEnd)) {
            date = now.toLocalDate().toString();
            shift = "Morning";
        } else if (currentTime.isAfter(morningEnd)) {
            date = now.toLocalDate().toString();
            shift = "Night";
        } else {
            date = now.minusDays(1).toLocalDate().toString();
            shift = "Night";
        }

        return new ShiftInfoDto(date, shift);
    }
}
