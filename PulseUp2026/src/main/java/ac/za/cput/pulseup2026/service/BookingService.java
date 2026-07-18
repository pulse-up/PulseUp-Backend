package ac.za.cput.pulseup2026.service;

public class BookingService {

import ac.za.cput.pulseup2026.domain.Appointment;
import ac.za.cput.pulseup2026.repository.AppointmentRepository;
import ac.za.cput.pulseup2026.repository.IAppointmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
public class BookingService implements IAppointmentRepository {
    private final AppointmentRepository appointmentRepository;

    public BookingService (AppointmentRepository appointmentRepository){
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public Appointment create(Appointment appointment) {
        if (appointment == null) return null;
        return appointmentRepository.save(appointment);
    }

    @Override
    public Appointment update(Appointment appointment) {
        if (appointment == null) return null;
        return appointmentRepository.save(appointment);
    }

    @Override
    public Appointment read(@PathVariable Long id) {
        return appointmentRepository.findById(id).orElse(null);
    }

    @Override
    public List<Appointment> getAll() {
        return appointmentRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        appointmentRepository.deleteById(id);
    }


    @Override
    public List<Appointment> readAll() {
        return appointmentRepository.findAll();
    }
}
