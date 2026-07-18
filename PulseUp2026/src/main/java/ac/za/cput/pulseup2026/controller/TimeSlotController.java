package ac.za.cput.pulseup2026.controller;

import ac.za.cput.pulseup2026.domain.TimeSlot;
import ac.za.cput.pulseup2026.service.TimeSlotService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/time-slots")
public class TimeSlotController {

    private final TimeSlotService timeSlotService;

    public TimeSlotController(TimeSlotService timeSlotService) {
        this.timeSlotService = timeSlotService;
    }

    @PostMapping
    public ResponseEntity<TimeSlot> createTimeSlot(
            @RequestBody TimeSlot timeSlot
    ) {
        TimeSlot savedTimeSlot =
                timeSlotService.saveTimeSlot(timeSlot);

        if (savedTimeSlot == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedTimeSlot);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TimeSlot> getTimeSlotById(
            @PathVariable Long id
    ) {
        TimeSlot timeSlot =
                timeSlotService.getTimeSlotById(id);

        if (timeSlot == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(timeSlot);
    }

    @GetMapping
    public ResponseEntity<List<TimeSlot>> getAllTimeSlots() {
        return ResponseEntity.ok(
                timeSlotService.getAllTimeSlots()
        );
    }

    @PutMapping
    public ResponseEntity<TimeSlot> updateTimeSlot(
            @RequestBody TimeSlot timeSlot
    ) {
        TimeSlot updatedTimeSlot =
                timeSlotService.updateTimeSlot(timeSlot);

        if (updatedTimeSlot == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(updatedTimeSlot);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTimeSlot(
            @PathVariable Long id
    ) {
        boolean deleted =
                timeSlotService.deleteTimeSlot(id);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}