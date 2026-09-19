package ac.za.cput.pulseup2026.repository;

import ac.za.cput.pulseup2026.domain.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimeSlotRepository
        extends JpaRepository<TimeSlot, Long> {

    List<TimeSlot>
    findByStaff_UserIdOrderBySlotDateAscStartTimeAsc(
            Long staffId
    );

    List<TimeSlot>
    findByAvailableTrueAndAppointmentTypeAndStaff_DepartmentIgnoreCaseOrderBySlotDateAscStartTimeAsc(
            String appointmentType,
            String department
    );

    List<TimeSlot>
    findAllByOrderBySlotDateAscStartTimeAsc();
}