package ac.za.cput.pulseup2026.factory;

import ac.za.cput.pulseup2026.domain.Staff;
import ac.za.cput.pulseup2026.domain.TimeSlot;

import java.time.LocalDate;
import java.time.LocalTime;

public final class TimeSlotFactory {

    private TimeSlotFactory() {
    }

    public static TimeSlot createTimeSlot(
            Staff staff,
            LocalDate slotDate,
            LocalTime startTime,
            LocalTime endTime
    ) {
        if (staff == null || slotDate == null
                || startTime == null || endTime == null
                || !startTime.isBefore(endTime)) {
            return null;
        }

        return TimeSlot.builder()
                .staff(staff)
                .slotDate(slotDate)
                .startTime(startTime)
                .endTime(endTime)
                .build();
    }
}