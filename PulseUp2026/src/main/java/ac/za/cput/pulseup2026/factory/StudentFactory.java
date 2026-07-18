package ac.za.cput.pulseup2026.factory;

import ac.za.cput.pulseup2026.domain.Student;

public final class StudentFactory {

    private StudentFactory() {
    }

    public static Student createStudent(String email, String passwordHash, String firstName, String lastName, String phoneNumber, String studentNumber, String course, String campus, String emergencyContactName, String emergencyContactPhone)
    {
        if (isNullOrEmpty(email) || isNullOrEmpty(passwordHash) || isNullOrEmpty(firstName) || isNullOrEmpty(lastName) || isNullOrEmpty(phoneNumber) || isNullOrEmpty(studentNumber)) {
            return null;
        }

        return Student.builder()
                .email(email)
                .passwordHash(passwordHash)
                .firstName(firstName)
                .lastName(lastName)
                .phoneNumber(phoneNumber)
                .studentNumber(studentNumber)
                .course(course)
                .campus(campus)
                .emergencyContactName(emergencyContactName)
                .emergencyContactPhone(emergencyContactPhone)
                .build();
    }

    private static boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
}