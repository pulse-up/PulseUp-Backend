package ac.za.cput.pulseup2026.domain;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "time_slots")
public class TimeSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long slotId;

    @ManyToOne
    @JoinColumn(name = "staff_id", nullable = false)
    private Staff staff;

    private LocalDate slotDate;

    private LocalTime startTime;

    private LocalTime endTime;

    private boolean available = true;

    protected TimeSlot() {
    }

    private TimeSlot(Builder builder) {
        this.staff = builder.staff;
        this.slotDate = builder.slotDate;
        this.startTime = builder.startTime;
        this.endTime = builder.endTime;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getSlotId() {
        return slotId;
    }

    public Staff getStaff() {
        return staff;
    }

    public LocalDate getSlotDate() {
        return slotDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public boolean isAvailable() {
        return available;
    }

    public void reserve() {
        this.available = false;
    }

    public void release() {
        this.available = true;
    }

    public static class Builder {

        private Staff staff;
        private LocalDate slotDate;
        private LocalTime startTime;
        private LocalTime endTime;

        public Builder staff(Staff staff) {
            this.staff = staff;
            return this;
        }

        public Builder slotDate(LocalDate slotDate) {
            this.slotDate = slotDate;
            return this;
        }

        public Builder startTime(LocalTime startTime) {
            this.startTime = startTime;
            return this;
        }

        public Builder endTime(LocalTime endTime) {
            this.endTime = endTime;
            return this;
        }

        public TimeSlot build() {
            return new TimeSlot(this);
        }
    }
}