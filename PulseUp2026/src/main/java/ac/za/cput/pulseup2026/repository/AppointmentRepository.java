package ac.za.cput.pulseup2026.repository;

import ac.za.cput.pulseup2026.domain.Appointment;
import ac.za.cput.pulseup2026.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository
        extends JpaRepository<Appointment, Long>
{

    List<Appointment> findByStudent(Student student);

    List<Appointment> findByStatus(String status);
}