package ac.za.cput.pulseup2026.service;

import ac.za.cput.pulseup2026.domain.Appointment;
import ac.za.cput.pulseup2026.domain.TimeSlot;
import ac.za.cput.pulseup2026.repository.AppointmentRepository;
import ac.za.cput.pulseup2026.repository.TimeSlotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final TimeSlotRepository timeSlotRepository;

    public AppointmentService(
            AppointmentRepository appointmentRepository,
            TimeSlotRepository timeSlotRepository
    ) {
        this.appointmentRepository = appointmentRepository;
        this.timeSlotRepository = timeSlotRepository;
    }

    @Transactional
    public Appointment saveAppointment(Appointment appointment) {
        if (appointment == null || appointment.getTimeSlot() == null) {
            return null;
        }

        TimeSlot timeSlot = appointment.getTimeSlot();

        if (!timeSlot.isAvailable()) {
            return null;
        }

        timeSlot.reserve();
        timeSlotRepository.save(timeSlot);

        return appointmentRepository.save(appointment);
    }

    public Appointment getAppointmentById(Long id) {
        return appointmentRepository.findById(id).orElse(null);
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public Appointment updateAppointmentStatus(Long id, String status) {
        Appointment appointment = getAppointmentById(id);

        if (appointment == null || status == null) {
            return null;
        }

        appointment.updateStatus(status);
        return appointmentRepository.save(appointment);
    }

    @Transactional
    public Appointment cancelAppointment(Long id) {
        Appointment appointment = getAppointmentById(id);

        if (appointment == null) {
            return null;
        }

        appointment.cancel();
        timeSlotRepository.save(appointment.getTimeSlot());

        return appointmentRepository.save(appointment);
    }

    public boolean deleteAppointment(Long id) {
        if (!appointmentRepository.existsById(id)) {
            return false;
        }

        appointmentRepository.deleteById(id);
        return true;
    }
}