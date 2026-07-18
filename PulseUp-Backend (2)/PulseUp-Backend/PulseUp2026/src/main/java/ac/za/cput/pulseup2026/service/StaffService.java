package ac.za.cput.pulseup2026.service;

import ac.za.cput.pulseup2026.domain.Staff;
import ac.za.cput.pulseup2026.repository.StaffRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StaffService {

    private final StaffRepository staffRepository;

    public StaffService(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    public Staff saveStaff(Staff staff) {
        if (staff == null) {
            return null;
        }

        return staffRepository.save(staff);
    }

    public Staff getStaffById(Long id) {
        return staffRepository.findById(id).orElse(null);
    }

    public List<Staff> getAllStaff() {
        return staffRepository.findAll();
    }

    public Staff updateStaff(Staff staff) {
        if (staff == null || staff.getUserId() == null) {
            return null;
        }

        return staffRepository.save(staff);
    }

    public boolean deleteStaff(Long id) {
        if (!staffRepository.existsById(id)) {
            return false;
        }

        staffRepository.deleteById(id);
        return true;
    }
}