package ac.za.cput.pulseup2026.service;

import ac.za.cput.pulseup2026.domain.Student;
import ac.za.cput.pulseup2026.repository.StudentRepository;
import ac.za.cput.pulseup2026.request.StudentProfileRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(
            StudentRepository studentRepository
    ) {
        this.studentRepository = studentRepository;
    }

    @Transactional
    public Student saveStudent(Student student) {
        if (
                student == null ||
                        isBlank(student.getStudentNumber())
        ) {
            return null;
        }

        String studentNumber =
                student.getStudentNumber().trim();

        if (
                studentRepository.existsByStudentNumber(
                        studentNumber
                )
        ) {
            return null;
        }

        return studentRepository.save(student);
    }

    public Student getStudentById(Long id) {
        if (id == null) {
            return null;
        }

        return studentRepository
                .findById(id)
                .orElse(null);
    }

    public Student getStudentByEmail(String email) {
        if (isBlank(email)) {
            return null;
        }

        return studentRepository
                .findByEmailIgnoreCase(
                        email.trim()
                )
                .orElse(null);
    }

    public List<Student> getAllStudents() {
        return studentRepository
                .findAllByActiveTrue();
    }

    public List<Student> getInactiveStudents() {
        return studentRepository
                .findAllByActiveFalse();
    }

    @Transactional
    public Student updateStudent(
            Student submittedStudent
    ) {
        if (
                submittedStudent == null ||
                        submittedStudent.getUserId() == null
        ) {
            return null;
        }

        Student existingStudent =
                studentRepository
                        .findById(
                                submittedStudent.getUserId()
                        )
                        .orElse(null);

        if (existingStudent == null) {
            return null;
        }

        existingStudent.updateProfile(
                cleanRequired(
                        submittedStudent.getFirstName()
                ),
                cleanRequired(
                        submittedStudent.getLastName()
                ),
                normaliseEmail(
                        submittedStudent.getEmail()
                ),
                cleanRequired(
                        submittedStudent.getPhoneNumber()
                ),
                cleanRequired(
                        submittedStudent.getCourse()
                ),
                cleanRequired(
                        submittedStudent.getCampus()
                ),
                cleanRequired(
                        submittedStudent.getResidence()
                ),
                submittedStudent.getYearOfStudy(),
                cleanOptional(
                        submittedStudent
                                .getEmergencyContactName()
                ),
                cleanOptional(
                        submittedStudent
                                .getEmergencyContactPhone()
                )
        );

        return studentRepository.save(
                existingStudent
        );
    }

    @Transactional
    public Student updateStudentProfileById(
            Long studentId,
            StudentProfileRequest request
    ) {
        if (
                studentId == null ||
                        !isValidProfileRequest(request)
        ) {
            return null;
        }

        Student student =
                studentRepository
                        .findById(studentId)
                        .orElse(null);

        if (
                student == null ||
                        !student.isActive()
        ) {
            return null;
        }

        applyProfileUpdate(
                student,
                request
        );

        return studentRepository.save(student);
    }

    @Transactional
    public Student updateCurrentStudentProfile(
            String authenticatedEmail,
            StudentProfileRequest request
    ) {
        if (
                isBlank(authenticatedEmail) ||
                        !isValidProfileRequest(request)
        ) {
            return null;
        }

        Student student =
                getStudentByEmail(
                        authenticatedEmail
                );

        if (
                student == null ||
                        !student.isActive()
        ) {
            return null;
        }

        applyProfileUpdate(
                student,
                request
        );

        return studentRepository.save(student);
    }

    @Transactional
    public boolean deleteStudent(Long id) {
        if (id == null) {
            return false;
        }

        Student student =
                studentRepository
                        .findById(id)
                        .orElse(null);

        if (student == null) {
            return false;
        }

        /*
         * Soft delete:
         * the student remains linked to existing
         * appointment records but cannot log in.
         */
        student.deactivate();

        studentRepository.save(student);

        return true;
    }

    @Transactional
    public Student restoreStudent(Long id) {
        if (id == null) {
            return null;
        }

        Student student =
                studentRepository
                        .findById(id)
                        .orElse(null);

        if (student == null) {
            return null;
        }

        student.activate();

        return studentRepository.save(student);
    }

    private void applyProfileUpdate(
            Student student,
            StudentProfileRequest request
    ) {
        student.updateProfile(
                request.getFirstName().trim(),
                request.getLastName().trim(),
                normaliseEmail(
                        request.getEmail()
                ),
                request.getPhoneNumber().trim(),
                request.getCourse().trim(),
                request.getCampus().trim(),
                request.getResidence().trim(),
                request.getYearOfStudy(),
                cleanOptional(
                        request.getEmergencyContactName()
                ),
                cleanOptional(
                        request.getEmergencyContactPhone()
                )
        );
    }

    private boolean isValidProfileRequest(
            StudentProfileRequest request
    ) {
        return request != null
                && !isBlank(
                request.getFirstName()
        )
                && !isBlank(
                request.getLastName()
        )
                && !isBlank(
                request.getEmail()
        )
                && !isBlank(
                request.getPhoneNumber()
        )
                && !isBlank(
                request.getCourse()
        )
                && !isBlank(
                request.getCampus()
        )
                && !isBlank(
                request.getResidence()
        )
                && request.getYearOfStudy() != null;
    }

    private String normaliseEmail(
            String email
    ) {
        if (email == null) {
            return "";
        }

        return email
                .trim()
                .toLowerCase();
    }

    private String cleanRequired(
            String value
    ) {
        return value == null
                ? ""
                : value.trim();
    }

    private String cleanOptional(
            String value
    ) {
        if (isBlank(value)) {
            return null;
        }

        return value.trim();
    }

    private boolean isBlank(
            String value
    ) {
        return value == null ||
                value.trim().isEmpty();
    }
}