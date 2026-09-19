package ac.za.cput.pulseup2026.controller;

import ac.za.cput.pulseup2026.domain.TimeSlot;
import ac.za.cput.pulseup2026.request.CreateTimeSlotRequest;
import ac.za.cput.pulseup2026.service.TimeSlotService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/time-slots")
public class TimeSlotController {

    private final TimeSlotService timeSlotService;

    public TimeSlotController(
            TimeSlotService timeSlotService
    ) {
        this.timeSlotService = timeSlotService;
    }

    @PostMapping(params = {
            "staffId",
            "slotDate",
            "startTime",
            "endTime",
            "appointmentType",
            "roomNumber"
    })
    public ResponseEntity<TimeSlot> createTimeSlot(
            @RequestParam Long staffId,

            @RequestParam
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE
            )
            LocalDate slotDate,

            @RequestParam
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.TIME
            )
            LocalTime startTime,

            @RequestParam
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.TIME
            )
            LocalTime endTime,

            @RequestParam String appointmentType,

            @RequestParam String roomNumber,

            @RequestParam(defaultValue = "20")
            int estimatedDurationMinutes
    ) {
        TimeSlot savedTimeSlot =
                timeSlotService.createTimeSlot(
                        staffId,
                        slotDate,
                        startTime,
                        endTime,
                        appointmentType,
                        roomNumber,
                        estimatedDurationMinutes
                );

        if (savedTimeSlot == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedTimeSlot);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TimeSlot> createTimeSlotFromJson(
            @RequestBody CreateTimeSlotRequest request
    ) {
        TimeSlot savedTimeSlot =
                timeSlotService.createTimeSlot(
                        request.getStaffId(),
                        request.getSlotDate(),
                        request.getStartTime(),
                        request.getEndTime(),
                        request.getAppointmentType(),
                        request.getRoomNumber(),
                        request.getEstimatedDurationMinutes()
                );

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
    public ResponseEntity<List<TimeSlot>> getTimeSlots(
            @RequestParam(required = false)
            String appointmentType
    ) {
        if (appointmentType != null
                && !appointmentType.isBlank()) {
            return ResponseEntity.ok(
                    timeSlotService
                            .getAvailableTimeSlotsByType(
                                    appointmentType
                            )
            );
        }

        return ResponseEntity.ok(
                timeSlotService.getAllTimeSlots()
        );
    }

    @GetMapping("/staff/{staffId}")
    public ResponseEntity<List<TimeSlot>> getStaffTimeSlots(
            @PathVariable Long staffId
    ) {
        return ResponseEntity.ok(
                timeSlotService.getTimeSlotsByStaffId(
                        staffId
                )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<TimeSlot> updateTimeSlot(
            @PathVariable Long id,

            @RequestParam Long staffId,

            @RequestParam
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE
            )
            LocalDate slotDate,

            @RequestParam
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.TIME
            )
            LocalTime startTime,

            @RequestParam
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.TIME
            )
            LocalTime endTime,

            @RequestParam String appointmentType,

            @RequestParam String roomNumber,

            @RequestParam(defaultValue = "20")
            int estimatedDurationMinutes
    ) {
        TimeSlot updatedTimeSlot =
                timeSlotService.updateTimeSlot(
                        id,
                        staffId,
                        slotDate,
                        startTime,
                        endTime,
                        appointmentType,
                        roomNumber,
                        estimatedDurationMinutes
                );

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