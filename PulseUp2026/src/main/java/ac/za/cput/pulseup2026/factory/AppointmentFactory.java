package ac.za.cput.pulseup2026.factory;

import ac.za.cput.pulseup2026.domain.Appointment;
import ac.za.cput.pulseup2026.domain.Staff;
import ac.za.cput.pulseup2026.domain.TimeSlot;
import ac.za.cput.pulseup2026.domain.User;

public class AppointmentFactory {

    public static Appointment createAppointment(
            User user,
            Staff staff,
            TimeSlot timeSlot,
            String appointmentType,
            String notes
    ) {

        return Appointment.builder()
                .user(user)
                .staff(staff)
                .timeSlot(timeSlot)
                .appointmentType(appointmentType)
                .status("PENDING")
                .notes(notes)
                .build();
    }
}
