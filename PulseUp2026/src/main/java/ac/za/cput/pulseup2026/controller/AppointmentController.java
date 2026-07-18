package ac.za.cput.pulseup2026.controller;

import ac.za.cput.pulseup2026.domain.Appointment;
import ac.za.cput.pulseup2026.service.AppointmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(
            AppointmentService appointmentService
    ) {
        this.appointmentService = appointmentService;
    }

    @PostMapping
    public ResponseEntity<Appointment> createAppointment(
            @RequestBody Appointment appointment
    ) {
        Appointment savedAppointment =
                appointmentService.saveAppointment(appointment);

        if (savedAppointment == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedAppointment);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointmentById(
            @PathVariable Long id
    ) {
        Appointment appointment =
                appointmentService.getAppointmentById(id);

        if (appointment == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(appointment);
    }

    @GetMapping
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        return ResponseEntity.ok(
                appointmentService.getAllAppointments()
        );
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Appointment> updateStatus(
            @PathVariable Long id,
            @RequestParam String status
    ) {
        Appointment updatedAppointment =
                appointmentService.updateAppointmentStatus(
                        id,
                        status
                );

        if (updatedAppointment == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(updatedAppointment);
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Appointment> cancelAppointment(
            @PathVariable Long id
    ) {
        Appointment cancelledAppointment =
                appointmentService.cancelAppointment(id);

        if (cancelledAppointment == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(cancelledAppointment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(
            @PathVariable Long id
    ) {
        boolean deleted =
                appointmentService.deleteAppointment(id);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}