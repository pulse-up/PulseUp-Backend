package ac.za.cput.pulseup2026.domain;

import jakarta.persistence.*;


@Entity

public class Staff extends User {
    
    private String staffId;
    private String department;
    private String position;
    private String specialization;
    private String availabilityStatus = "AVAILABLE";
    
    // Private constructor for Builder pattern
    private Staff(StaffBuilder builder) {
        super();
        this.staffId = builder.staffId;
        this.department = builder.department;
        this.position = builder.position;
        this.specialization = builder.specialization;
        this.availabilityStatus = builder.availabilityStatus;
    }
    
    // No-arg constructor for JPA/Hibernate
    public Staff() {
        super();
    }
    

    public static StaffBuilder staffBuilder() {
        return new StaffBuilder();
    }
    
    public String getStaffId() {
        return staffId;
    }
    
    public String getDepartment() {
        return department;
    }
    
    public String getPosition() {
        return position;
    }
    
    public String getSpecialization() {
        return specialization;
    }
    
    public String getAvailabilityStatus() {
        return availabilityStatus;
    }
    
    @Override
    public String toString() {
        return "Staff{" +
                "staffId='" + staffId + '\'' +
                ", department='" + department + '\'' +
                ", position='" + position + '\'' +
                ", specialization='" + specialization + '\'' +
                ", availabilityStatus='" + availabilityStatus + '\'' +
                ", email='" + this.getEmail() + '\'' +
                '}';
    }


    public static class StaffBuilder {
        private String staffId;
        private String department;
        private String position;
        private String specialization;
        private String availabilityStatus = "AVAILABLE";

        public StaffBuilder() {
        }

        public StaffBuilder staffId(String staffId) {
            this.staffId = staffId;
            return this;
        }

        public StaffBuilder department(String department) {
            this.department = department;
            return this;
        }

        public StaffBuilder position(String position) {
            this.position = position;
            return this;
        }

        public StaffBuilder specialization(String specialization) {
            this.specialization = specialization;
            return this;
        }

        public StaffBuilder availabilityStatus(String availabilityStatus) {
            this.availabilityStatus = availabilityStatus;
            return this;
        }


        public Staff build() {
            return new Staff(this);
        }
    }
}

