package ac.za.cput.pulseup2026.repository;

import ac.za.cput.pulseup2026.domain.Appointment;

import java.util.List;

public interface IAppointmentRepository extends IRepository<Appointment, Long>{

    Appointment read(Long id);
    List<Appointment> readAll();
    Appointment create(Appointment appointment);
    Appointment update(Appointment appointment);

    List<Appointment> getAll();
    void delete(Long id);
}
