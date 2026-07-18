package ac.za.cput.pulseup2026.controller;

import ac.za.cput.pulseup2026.domain.Staff;
import ac.za.cput.pulseup2026.service.StaffService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/staff")
public class StaffController {

    private final StaffService staffService;

    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @PostMapping
    public ResponseEntity<Staff> createStaff(
            @RequestBody Staff staff
    ) {
        Staff savedStaff = staffService.saveStaff(staff);

        if (savedStaff == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedStaff);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Staff> getStaffById(
            @PathVariable Long id
    ) {
        Staff staff = staffService.getStaffById(id);

        if (staff == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(staff);
    }

    @GetMapping
    public ResponseEntity<List<Staff>> getAllStaff() {
        return ResponseEntity.ok(
                staffService.getAllStaff()
        );
    }

    @PutMapping
    public ResponseEntity<Staff> updateStaff(
            @RequestBody Staff staff
    ) {
        Staff updatedStaff = staffService.updateStaff(staff);

        if (updatedStaff == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(updatedStaff);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStaff(
            @PathVariable Long id
    ) {
        boolean deleted = staffService.deleteStaff(id);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}