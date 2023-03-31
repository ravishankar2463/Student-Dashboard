package com.example.security.models;

import com.example.models.Student;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    private String emailId;
    private String password;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="student_id",unique = true)
    private Student student;
    private boolean enabled;

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
