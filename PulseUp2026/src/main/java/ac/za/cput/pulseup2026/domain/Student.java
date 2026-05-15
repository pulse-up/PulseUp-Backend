package ac.za.cput.pulseup2026.domain;

import jakarta.persistence.*;


@Entity

public class Student extends User {
    private String studId;
    private String name;
    private String email;
    private String password;
    private String number;

    //  Builder pattern
    private Student(Builder builder) {
        super();
        this.studId = builder.studId;
        this.name = builder.name;
        this.email = builder.email;
        this.password = builder.password;
        this.number = builder.number;
    }

    public Student() {
        super();
    }

    public void setStudId(String studId) {
        this.studId = studId;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Student{" +
                "studId='" + studId + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", number='" + number + '\'' +
                '}';
    }

    public static class Builder{
        private String studId;
        private String name;
        private String email;
        private String password;
        private String number;
        public Builder() {}
        public Builder studId(String studId) {
            this.studId = studId;
            return this;
        }
        public Builder name(String name) {
            this.name = name;
            return this;
        }
        public Builder email(String email) {
            this.email = email;
            return this;
        }
        public Builder password(String password) {
            this.password = password;
            return this;
        }
        public Builder number(String number) {
            this.number = number;
            return this;
        }
        public Student build() {
            return new Student(this);

        }
    }

}



