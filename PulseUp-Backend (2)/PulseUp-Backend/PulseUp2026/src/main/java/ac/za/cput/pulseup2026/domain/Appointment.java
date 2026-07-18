package ac.za.cput.pulseup2026.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appointmentId;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "slot_id", nullable = false)
    private TimeSlot timeSlot;

    private String appointmentType;

    private String status = "PENDING";

    private String notes;

    private LocalDateTime createdAt = LocalDateTime.now();

    protected Appointment() {
    }

    private Appointment(Builder builder) {
        this.student = builder.student;
        this.timeSlot = builder.timeSlot;
        this.appointmentType = builder.appointmentType;
        this.notes = builder.notes;
        this.status = "PENDING";
        this.createdAt = LocalDateTime.now();
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getAppointmentId() {
        return appointmentId;
    }

    public Student getStudent() {
        return student;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public String getAppointmentType() {
        return appointmentType;
    }

    public String getStatus() {
        return status;
    }

    public String getNotes() {
        return notes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void updateStatus(String status) {
        this.status = status;
    }

    public void cancel() {
        this.status = "CANCELLED";
        this.timeSlot.release();
    }

    public static class Builder {

        private Student student;
        private TimeSlot timeSlot;
        private String appointmentType;
        private String notes;

        public Builder student(Student student) {
            this.student = student;
            return this;
        }

        public Builder timeSlot(TimeSlot timeSlot) {
            this.timeSlot = timeSlot;
            return this;
        }

        public Builder appointmentType(String appointmentType) {
            this.appointmentType = appointmentType;
            return this;
        }

        public Builder notes(String notes) {
            this.notes = notes;
            return this;
        }

        public Appointment build() {
            return new Appointment(this);
        }
    }
}