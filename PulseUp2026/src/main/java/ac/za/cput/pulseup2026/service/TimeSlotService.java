package ac.za.cput.pulseup2026.service;

import ac.za.cput.pulseup2026.domain.AppointmentType;
import ac.za.cput.pulseup2026.domain.Staff;
import ac.za.cput.pulseup2026.domain.TimeSlot;
import ac.za.cput.pulseup2026.factory.TimeSlotFactory;
import ac.za.cput.pulseup2026.repository.StaffRepository;
import ac.za.cput.pulseup2026.repository.TimeSlotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@Transactional
public class TimeSlotService {

    private final TimeSlotRepository timeSlotRepository;
    private final StaffRepository staffRepository;

    public TimeSlotService(
            TimeSlotRepository timeSlotRepository,
            StaffRepository staffRepository
    ) {
        this.timeSlotRepository = timeSlotRepository;
        this.staffRepository = staffRepository;
    }

    public TimeSlot createTimeSlot(
            Long staffId,
            LocalDate slotDate,
            LocalTime startTime,
            LocalTime endTime,
            String appointmentType,
            String roomNumber,
            int estimatedDurationMinutes
    ) {
        if (staffId == null) {
            return null;
        }

        Staff staff = staffRepository
                .findById(staffId)
                .orElse(null);

        if (staff == null) {
            return null;
        }

        TimeSlot timeSlot = TimeSlotFactory.createTimeSlot(
                staff,
                slotDate,
                startTime,
                endTime,
                appointmentType,
                roomNumber,
                estimatedDurationMinutes
        );

        if (timeSlot == null) {
            return null;
        }

        return timeSlotRepository.save(timeSlot);
    }

    @Transactional(readOnly = true)
    public TimeSlot getTimeSlotById(Long id) {
        if (id == null) {
            return null;
        }

        return timeSlotRepository
                .findById(id)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public List<TimeSlot> getAllTimeSlots() {
        return timeSlotRepository
                .findAllByOrderBySlotDateAscStartTimeAsc();
    }

    @Transactional(readOnly = true)
    public List<TimeSlot> getAvailableTimeSlotsByType(
            String appointmentTypeValue
    ) {
        AppointmentType appointmentType =
                AppointmentType.fromValue(
                        appointmentTypeValue
                );

        if (appointmentType == null) {
            return List.of();
        }

        return timeSlotRepository
                .findByAvailableTrueAndAppointmentTypeAndStaff_DepartmentIgnoreCaseOrderBySlotDateAscStartTimeAsc(
                        appointmentType.getDisplayName(),
                        appointmentType.getRequiredDepartment()
                );
    }

    public TimeSlot updateTimeSlot(
            Long slotId,
            Long staffId,
            LocalDate slotDate,
            LocalTime startTime,
            LocalTime endTime,
            String appointmentTypeValue,
            String roomNumber,
            int estimatedDurationMinutes
    ) {
        if (slotId == null || staffId == null) {
            return null;
        }

        TimeSlot existingTimeSlot = timeSlotRepository
                .findById(slotId)
                .orElse(null);

        if (existingTimeSlot == null) {
            return null;
        }

        Staff staff = staffRepository
                .findById(staffId)
                .orElse(null);

        if (staff == null) {
            return null;
        }

        AppointmentType appointmentType =
                AppointmentType.fromValue(
                        appointmentTypeValue
                );

        if (slotDate == null
                || startTime == null
                || endTime == null
                || roomNumber == null
                || roomNumber.isBlank()
                || estimatedDurationMinutes <= 0
                || estimatedDurationMinutes > 180
                || !startTime.isBefore(endTime)
                || appointmentType == null
                || !appointmentType.isAllowedForDepartment(
                staff.getDepartment())) {
            return null;
        }

        existingTimeSlot.updateDetails(
                staff,
                slotDate,
                startTime,
                endTime,
                appointmentType.getDisplayName(),
                roomNumber.trim(),
                estimatedDurationMinutes
        );

        return timeSlotRepository.save(existingTimeSlot);
    }

    public boolean deleteTimeSlot(Long id) {
        if (id == null
                || !timeSlotRepository.existsById(id)) {
            return false;
        }

        timeSlotRepository.deleteById(id);
        return true;
    }

    @Transactional(readOnly = true)
    public List<TimeSlot> getTimeSlotsByStaffId(
            Long staffId
    ) {
        if (staffId == null) {
            return List.of();
        }

        return timeSlotRepository
                .findByStaff_UserIdOrderBySlotDateAscStartTimeAsc(
                        staffId
                );
    }
}