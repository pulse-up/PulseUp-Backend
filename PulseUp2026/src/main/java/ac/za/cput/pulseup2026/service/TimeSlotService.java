package ac.za.cput.pulseup2026.service;

import ac.za.cput.pulseup2026.domain.TimeSlot;
import ac.za.cput.pulseup2026.repository.TimeSlotRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TimeSlotService {

    private final TimeSlotRepository timeSlotRepository;

    public TimeSlotService(TimeSlotRepository timeSlotRepository) {
        this.timeSlotRepository = timeSlotRepository;
    }

    public TimeSlot saveTimeSlot(TimeSlot timeSlot) {
        if (timeSlot == null) {
            return null;
        }

        return timeSlotRepository.save(timeSlot);
    }

    public TimeSlot getTimeSlotById(Long id) {
        return timeSlotRepository.findById(id).orElse(null);
    }

    public List<TimeSlot> getAllTimeSlots() {
        return timeSlotRepository.findAll();
    }

    public TimeSlot updateTimeSlot(TimeSlot timeSlot) {
        if (timeSlot == null || timeSlot.getSlotId() == null) {
            return null;
        }

        return timeSlotRepository.save(timeSlot);
    }

    public boolean deleteTimeSlot(Long id) {
        if (!timeSlotRepository.existsById(id)) {
            return false;
        }

        timeSlotRepository.deleteById(id);
        return true;
    }
}