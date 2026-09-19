package ac.za.cput.pulseup2026.controller;

import ac.za.cput.pulseup2026.domain.Appointment;
import ac.za.cput.pulseup2026.response.AppointmentQueueResponse;
import ac.za.cput.pulseup2026.service.AppointmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
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

    /*
     * studentId and appointmentType are intentionally
     * not accepted. The JWT identifies the student and
     * the selected slot determines the appointment type.
     */
    @PostMapping
    public ResponseEntity<Appointment> createAppointment(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam Long slotId,
            @RequestParam(required = false) String notes
    ) {
        Long studentId = getUserId(jwt);

        if (studentId == null || !hasRole(jwt, "STUDENT")) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .build();
        }

        Appointment savedAppointment =
                appointmentService.createAppointment(
                        studentId,
                        slotId,
                        notes
                );

        if (savedAppointment == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedAppointment);
    }

    @GetMapping("/my-queue")
    public ResponseEntity<List<AppointmentQueueResponse>>
    getMyQueue(
            @AuthenticationPrincipal Jwt jwt
    ) {
        Long studentId = getUserId(jwt);

        if (studentId == null || !hasRole(jwt, "STUDENT")) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .build();
        }

        return ResponseEntity.ok(
                appointmentService.getQueueForStudent(
                        studentId
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointmentById(
            @PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt
    ) {
        Appointment appointment =
                appointmentService.getAppointmentById(id);

        if (appointment == null) {
            return ResponseEntity.notFound().build();
        }

        boolean allowed = appointmentService
                .canViewAppointment(
                        appointment,
                        getUserId(jwt),
                        hasRole(jwt, "STUDENT"),
                        hasRole(jwt, "STAFF"),
                        hasRole(jwt, "ADMIN")
                );

        if (!allowed) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .build();
        }

        return ResponseEntity.ok(appointment);
    }

    /*
     * SecurityConfig already limits this endpoint to ADMIN.
     */
    @GetMapping
    public ResponseEntity<List<Appointment>>
    getAllAppointments() {
        return ResponseEntity.ok(
                appointmentService.getAllAppointments()
        );
    }

    /*
     * Compatible with the current student dashboard.
     * A student can only request their own ID.
     */
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Appointment>>
    getStudentAppointments(
            @PathVariable Long studentId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        boolean adminUser = hasRole(jwt, "ADMIN");

        if (!adminUser
                && (!hasRole(jwt, "STUDENT")
                || !studentId.equals(getUserId(jwt)))) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .build();
        }

        return ResponseEntity.ok(
                appointmentService
                        .getAppointmentsByStudentId(studentId)
        );
    }

    /*
     * Compatible with the current staff dashboard.
     * Staff can only request their own schedule.
     */
    @GetMapping("/staff/{staffId}")
    public ResponseEntity<List<Appointment>>
    getStaffAppointments(
            @PathVariable Long staffId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        boolean adminUser = hasRole(jwt, "ADMIN");

        if (!adminUser
                && (!hasRole(jwt, "STAFF")
                || !staffId.equals(getUserId(jwt)))) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .build();
        }

        return ResponseEntity.ok(
                appointmentService
                        .getAppointmentsByStaffId(staffId)
        );
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Appointment> updateStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @AuthenticationPrincipal Jwt jwt
    ) {
        boolean adminUser = hasRole(jwt, "ADMIN");
        boolean staffUser = hasRole(jwt, "STAFF");

        if (!adminUser && !staffUser) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .build();
        }

        Appointment updatedAppointment =
                appointmentService.updateAppointmentStatus(
                        id,
                        status,
                        getUserId(jwt),
                        adminUser
                );

        if (updatedAppointment == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(updatedAppointment);
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Appointment> cancelAppointment(
            @PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt
    ) {
        boolean adminUser = hasRole(jwt, "ADMIN");

        if (!adminUser && !hasRole(jwt, "STUDENT")) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .build();
        }

        Appointment cancelledAppointment =
                appointmentService.cancelAppointment(
                        id,
                        getUserId(jwt),
                        adminUser
                );

        if (cancelledAppointment == null) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .build();
        }

        return ResponseEntity.ok(cancelledAppointment);
    }

    @PatchMapping("/{id}/reschedule")
    public ResponseEntity<Appointment>
    rescheduleAppointment(
            @PathVariable Long id,
            @RequestParam Long slotId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        boolean adminUser = hasRole(jwt, "ADMIN");

        if (!adminUser && !hasRole(jwt, "STUDENT")) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .build();
        }

        Appointment rescheduledAppointment =
                appointmentService.rescheduleAppointment(
                        id,
                        slotId,
                        getUserId(jwt),
                        adminUser
                );

        if (rescheduledAppointment == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(rescheduledAppointment);
    }

    /*
     * SecurityConfig already limits deletion to ADMIN.
     */
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

    private Long getUserId(Jwt jwt) {
        if (jwt == null) {
            return null;
        }

        Object userIdClaim = jwt.getClaim("userId");

        if (userIdClaim instanceof Number number) {
            return number.longValue();
        }

        if (userIdClaim instanceof String value) {
            try {
                return Long.parseLong(value);
            } catch (NumberFormatException exception) {
                return null;
            }
        }

        return null;
    }

    private boolean hasRole(Jwt jwt, String role) {
        if (jwt == null) {
            return false;
        }

        List<String> roles =
                jwt.getClaimAsStringList("roles");

        return roles != null && roles.contains(role);
    }
}