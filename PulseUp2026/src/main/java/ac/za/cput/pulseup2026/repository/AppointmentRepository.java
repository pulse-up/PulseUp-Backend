package ac.za.cput.pulseup2026.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ac.za.cput.pulseup2026.domain.Appointment;

@Repository
public interface AppointmentRepository extends IRepository<Appointment, Long>{
}
