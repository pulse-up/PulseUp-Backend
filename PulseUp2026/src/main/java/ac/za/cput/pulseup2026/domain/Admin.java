package ac.za.cput.pulseup2026.domain;

import jakarta.persistence.*;


@Entity
public class Admin extends User {
    
    private String adminId;
    private String role;
    private String permissions;
    private String department;
    private Boolean canManageUsers = false;
    private Boolean canManageAppointments = false;

    
    // Private constructor for Builder pattern
    private Admin(AdminBuilder builder) {
        super();
        this.adminId = builder.adminId;
        this.role = builder.role;
        this.permissions = builder.permissions;
        this.department = builder.department;
        this.canManageUsers = builder.canManageUsers;
        this.canManageAppointments = builder.canManageAppointments;

    }


    public Admin() {
        super();
    }
    

    public static AdminBuilder adminBuilder() {
        return new AdminBuilder();
    }
    
    public String getAdminId() {
        return adminId;
    }
    
    public String getRole() {
        return role;
    }
    
    public String getPermissions() {
        return permissions;
    }
    
    public String getDepartment() {
        return department;
    }
    
    public Boolean getCanManageUsers() {
        return canManageUsers;
    }
    
    public Boolean getCanManageAppointments() {
        return canManageAppointments;
    }
    

    
    @Override
    public String toString() {
        return "Admin{" +
                "adminId='" + adminId + '\'' +
                ", role='" + role + '\'' +
                ", department='" + department + '\'' +
                ", canManageUsers=" + canManageUsers +
                ", canManageAppointments=" + canManageAppointments +
                ", email='" + this.getEmail() + '\'' +
                '}';
    }


    public static class AdminBuilder {
        private String adminId;
        private String role;
        private String permissions;
        private String department;
        private Boolean canManageUsers = false;
        private Boolean canManageAppointments = false;


        public AdminBuilder() {
        }

        public AdminBuilder adminId(String adminId) {
            this.adminId = adminId;
            return this;
        }

        public AdminBuilder role(String role) {
            this.role = role;
            return this;
        }

        public AdminBuilder permissions(String permissions) {
            this.permissions = permissions;
            return this;
        }

        public AdminBuilder department(String department) {
            this.department = department;
            return this;
        }

        public AdminBuilder canManageUsers(Boolean canManageUsers) {
            this.canManageUsers = canManageUsers;
            return this;
        }

        public AdminBuilder canManageAppointments(Boolean canManageAppointments) {
            this.canManageAppointments = canManageAppointments;
            return this;
        }



        public Admin build() {
            return new Admin(this);
        }
    }
}

