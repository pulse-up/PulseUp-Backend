package ac.za.cput.pulseup2026.controller;

import ac.za.cput.pulseup2026.domain.Student;
import ac.za.cput.pulseup2026.request.StudentProfileRequest;
import ac.za.cput.pulseup2026.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(
            StudentService studentService
    ) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<Student> createStudent(
            @RequestBody Student student
    ) {
        Student savedStudent =
                studentService.saveStudent(student);

        if (savedStudent == null) {
            return ResponseEntity
                    .badRequest()
                    .build();
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedStudent);
    }

    /*
     * Loads the student represented by the JWT.
     * No browser-stored user ID is required.
     */
    @GetMapping("/me")
    public ResponseEntity<Student> getCurrentStudent(
            Authentication authentication
    ) {
        if (
                authentication == null ||
                        !authentication.isAuthenticated()
        ) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .build();
        }

        Student student =
                studentService.getStudentByEmail(
                        authentication.getName()
                );

        if (
                student == null ||
                        !student.isActive()
        ) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        return ResponseEntity.ok(student);
    }

    /*
     * Updates only the currently authenticated student.
     */
    @PutMapping("/me/profile")
    public ResponseEntity<Student>
    updateCurrentStudentProfile(
            Authentication authentication,
            @RequestBody StudentProfileRequest request
    ) {
        if (
                authentication == null ||
                        !authentication.isAuthenticated()
        ) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .build();
        }

        Student updatedStudent =
                studentService
                        .updateCurrentStudentProfile(
                                authentication.getName(),
                                request
                        );

        if (updatedStudent == null) {
            return ResponseEntity
                    .badRequest()
                    .build();
        }

        return ResponseEntity.ok(updatedStudent);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(
            @PathVariable Long id
    ) {
        Student student =
                studentService.getStudentById(id);

        if (student == null) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        return ResponseEntity.ok(student);
    }

    @GetMapping
    public ResponseEntity<List<Student>>
    getAllStudents() {
        return ResponseEntity.ok(
                studentService.getAllStudents()
        );
    }

    @GetMapping("/inactive/list")
    public ResponseEntity<List<Student>>
    getInactiveStudents() {
        return ResponseEntity.ok(
                studentService.getInactiveStudents()
        );
    }

    @PutMapping
    public ResponseEntity<Student> updateStudent(
            @RequestBody Student student
    ) {
        Student updatedStudent =
                studentService.updateStudent(student);

        if (updatedStudent == null) {
            return ResponseEntity
                    .badRequest()
                    .build();
        }

        return ResponseEntity.ok(updatedStudent);
    }

    @PutMapping("/{id}/profile")
    public ResponseEntity<Student>
    updateStudentProfileById(
            @PathVariable Long id,
            @RequestBody StudentProfileRequest request
    ) {
        Student updatedStudent =
                studentService.updateStudentProfileById(
                        id,
                        request
                );

        if (updatedStudent == null) {
            return ResponseEntity
                    .badRequest()
                    .build();
        }

        return ResponseEntity.ok(updatedStudent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(
            @PathVariable Long id
    ) {
        boolean deleted =
                studentService.deleteStudent(id);

        if (!deleted) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        return ResponseEntity
                .noContent()
                .build();
    }

    @PatchMapping("/{id}/restore")
    public ResponseEntity<Student> restoreStudent(
            @PathVariable Long id
    ) {
        Student restoredStudent =
                studentService.restoreStudent(id);

        if (restoredStudent == null) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        return ResponseEntity.ok(restoredStudent);
    }
}