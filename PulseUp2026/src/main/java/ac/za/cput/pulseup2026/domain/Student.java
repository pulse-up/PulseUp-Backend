package ac.za.cput.pulseup2026.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "students")
public class Student extends User {

    @Column(nullable = false, unique = true)
    private String studentNumber;

    private String course;

    private String campus;

    private String emergencyContactName;

    private String emergencyContactPhone;

    private int healthPoints;

    protected Student() {
    }

    private Student(Builder builder) {
        super(
                builder.email,
                builder.passwordHash,
                builder.firstName,
                builder.lastName,
                builder.phoneNumber
        );

        this.studentNumber = builder.studentNumber;
        this.course = builder.course;
        this.campus = builder.campus;
        this.emergencyContactName = builder.emergencyContactName;
        this.emergencyContactPhone = builder.emergencyContactPhone;
        this.healthPoints = 0;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public String getCourse() {
        return course;
    }

    public String getCampus() {
        return campus;
    }

    public String getEmergencyContactName() {
        return emergencyContactName;
    }

    public String getEmergencyContactPhone() {
        return emergencyContactPhone;
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public void addHealthPoints(int points) {
        this.healthPoints += points;
    }

    public static class Builder {

        private String email;
        private String passwordHash;
        private String firstName;
        private String lastName;
        private String phoneNumber;
        private String studentNumber;
        private String course;
        private String campus;
        private String emergencyContactName;
        private String emergencyContactPhone;

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder passwordHash(String passwordHash) {
            this.passwordHash = passwordHash;
            return this;
        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Builder studentNumber(String studentNumber) {
            this.studentNumber = studentNumber;
            return this;
        }

        public Builder course(String course) {
            this.course = course;
            return this;
        }

        public Builder campus(String campus) {
            this.campus = campus;
            return this;
        }

        public Builder emergencyContactName(String emergencyContactName) {
            this.emergencyContactName = emergencyContactName;
            return this;
        }

        public Builder emergencyContactPhone(String emergencyContactPhone) {
            this.emergencyContactPhone = emergencyContactPhone;
            return this;
        }

        public Student build() {
            return new Student(this);
        }
    }
}