package ac.za.cput.pulseup2026.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;


@Embeddable
public class TimeSlot {
    
    private LocalTime startTime;
    private LocalTime endTime;
    private String dayOfWeek;
    private Boolean isAvailable = true;
    private LocalDateTime slotDate;
    
    public TimeSlot() {
    }
    
    public TimeSlot(LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }
    
    public LocalTime getStartTime() {
        return startTime;
    }
    
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }
    
    public LocalTime getEndTime() {
        return endTime;
    }
    
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
    
    public String getDayOfWeek() {
        return dayOfWeek;
    }
    
    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
    
    public Boolean getIsAvailable() {
        return isAvailable;
    }
    
    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
    
    public LocalDateTime getSlotDate() {
        return slotDate;
    }
    
    public void setSlotDate(LocalDateTime slotDate) {
        this.slotDate = slotDate;
    }
    
    public boolean isValidTimeSlot() {
        return startTime != null && endTime != null && startTime.isBefore(endTime);
    }
    
    public boolean containsTime(LocalTime time) {
        return time != null && !time.isBefore(startTime) && !time.isAfter(endTime);
    }
    
    @Override
    public String toString() {
        return "TimeSlot{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                ", dayOfWeek='" + dayOfWeek + '\'' +
                ", isAvailable=" + isAvailable +
                '}';
    }
}

