package ac.za.cput.pulseup2026.factory;

import ac.za.cput.pulseup2026.domain.Student;
import ac.za.cput.pulseup2026.domain.YearOfStudy;

public final class StudentFactory {

    private StudentFactory() {
    }

    public static Student createStudent(
            String email,
            String passwordHash,
            String firstName,
            String lastName,
            String phoneNumber,
            String studentNumber,
            String course,
            String campus,
            String residence,
            YearOfStudy yearOfStudy,
            String emergencyContactName,
            String emergencyContactPhone
    ) {
        if (
                isNullOrEmpty(email) ||
                        isNullOrEmpty(passwordHash) ||
                        isNullOrEmpty(firstName) ||
                        isNullOrEmpty(lastName) ||
                        isNullOrEmpty(phoneNumber) ||
                        isNullOrEmpty(studentNumber) ||
                        isNullOrEmpty(course) ||
                        isNullOrEmpty(campus) ||
                        isNullOrEmpty(residence) ||
                        yearOfStudy == null ||
                        isNullOrEmpty(emergencyContactName) ||
                        isNullOrEmpty(emergencyContactPhone)
        ) {
            return null;
        }

        return Student.builder()
                .email(email.trim().toLowerCase())
                .passwordHash(passwordHash)
                .firstName(firstName.trim())
                .lastName(lastName.trim())
                .phoneNumber(phoneNumber.trim())
                .studentNumber(studentNumber.trim())
                .course(course.trim())
                .campus(campus.trim())
                .residence(residence.trim())
                .yearOfStudy(yearOfStudy)
                .emergencyContactName(
                        emergencyContactName.trim()
                )
                .emergencyContactPhone(
                        emergencyContactPhone.trim()
                )
                .build();
    }

    private static boolean isNullOrEmpty(
            String value
    ) {
        return value == null ||
                value.trim().isEmpty();
    }
}