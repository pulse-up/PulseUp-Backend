package ac.za.cput.pulseup2026.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;



public class Appointment {
    
import java.time.LocalDateTime;


@Entity
@Table(name = "appointments")

public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appointmentId;

    // Student/User who booked the appointment
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Staff member handling the appointment
    @ManyToOne
    @JoinColumn(name = "staff_id")
    private Staff staff;

    // Embedded TimeSlot
    @Embedded
    private TimeSlot timeSlot;

    private String appointmentType;
    private String status;
    private String notes;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Default constructor required by JPA
    public Appointment() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Builder constructor
    private Appointment(AppointmentBuilder builder) {
        this.appointmentId = builder.appointmentId;
        this.user = builder.user;
        this.staff = builder.staff;
        this.timeSlot = builder.timeSlot;
        this.appointmentType = builder.appointmentType;
        this.status = builder.status;
        this.notes = builder.notes;
        this.createdAt = builder.createdAt != null
                ? builder.createdAt
                : LocalDateTime.now();

        this.updatedAt = builder.updatedAt != null
                ? builder.updatedAt
                : LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Builder method
    public static AppointmentBuilder builder() {
        return new AppointmentBuilder();
    }

    // Getters
    public Long getAppointmentId() {
        return appointmentId;
    }

    public User getUser() {
        return user;
    }

    public Staff getStaff() {
        return staff;
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

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "appointmentId=" + appointmentId +
                ", appointmentType='" + appointmentType + '\'' +
                ", status='" + status + '\'' +
                ", user=" + user +
                ", staff=" + staff +
                ", timeSlot=" + timeSlot +
                '}';
    }

    // Builder Class
    public static class AppointmentBuilder {

        private Long appointmentId;
        private User user;
        private Staff staff;
        private TimeSlot timeSlot;
        private String appointmentType;
        private String status = "PENDING";
        private String notes;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public AppointmentBuilder appointmentId(Long appointmentId) {
            this.appointmentId = appointmentId;
            return this;
        }

        public AppointmentBuilder user(User user) {
            this.user = user;
            return this;
        }

        public AppointmentBuilder staff(Staff staff) {
            this.staff = staff;
            return this;
        }

        public AppointmentBuilder timeSlot(TimeSlot timeSlot) {
            this.timeSlot = timeSlot;
            return this;
        }

        public AppointmentBuilder appointmentType(String appointmentType) {
            this.appointmentType = appointmentType;
            return this;
        }

        public AppointmentBuilder status(String status) {
            this.status = status;
            return this;
        }

        public AppointmentBuilder notes(String notes) {
            this.notes = notes;
            return this;
        }

        public AppointmentBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public AppointmentBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Appointment build() {
            return new Appointment(this);
        }
    }

}

