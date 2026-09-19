package ac.za.cput.pulseup2026.controller;

import ac.za.cput.pulseup2026.request.AuthResponse;
import ac.za.cput.pulseup2026.request.LoginRequest;
import ac.za.cput.pulseup2026.request.RegisterAdminRequest;
import ac.za.cput.pulseup2026.request.RegisterStaffRequest;
import ac.za.cput.pulseup2026.request.RegisterStudentRequest;
import ac.za.cput.pulseup2026.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register/student")
    public ResponseEntity<AuthResponse> registerStudent(
            @RequestBody RegisterStudentRequest request
    ) {
        AuthResponse response =
                authService.registerStudent(request);

        if (response == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/register/staff")
    public ResponseEntity<AuthResponse> registerStaff(
            @RequestBody RegisterStaffRequest request
    ) {
        AuthResponse response =
                authService.registerStaff(request);

        if (response == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/register/admin")
    public ResponseEntity<AuthResponse> registerAdmin(
            @RequestBody RegisterAdminRequest request
    ) {
        AuthResponse response =
                authService.registerAdmin(request);

        if (response == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    /*
     * Public only while the admins table is empty.
     * After the first successful bootstrap, it returns 403.
     */
    @PostMapping("/bootstrap-admin")
    public ResponseEntity<AuthResponse> bootstrapFirstAdmin(
            @RequestBody RegisterAdminRequest request
    ) {
        AuthResponse response =
                authService.bootstrapFirstAdmin(request);

        if (response == null) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .build();
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody LoginRequest request
    ) {
        AuthResponse response = authService.login(request);

        if (response == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .build();
        }

        return ResponseEntity.ok(response);
    }
}