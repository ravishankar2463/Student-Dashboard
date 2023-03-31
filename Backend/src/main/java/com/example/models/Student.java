package com.example.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="id")
    private Integer id;
    @Column(unique = true,nullable = false)
    private String emailId;
    private String name;
    private LocalDate dateOfBirth;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(name = "students_courses",
            joinColumns = {
                    @JoinColumn(name = "student_id", referencedColumnName = "id",
                            nullable = false, updatable = false)},
            inverseJoinColumns = {
                    @JoinColumn(name = "course_id", referencedColumnName = "id",
                            nullable = false, updatable = false)})
    private Set<Course> subscribedCourses = new HashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateOfBirth() {
        if(dateOfBirth != null){
            return dateOfBirth.toString();
        }

        return "";
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    @JsonIgnore
    public Set<Course> getSubscribedCourses() {
        return subscribedCourses;
    }

    public void setSubscribedCourses(Set<Course> subscribedCourses) {
        this.subscribedCourses = subscribedCourses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return getId().equals(student.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", emailId='" + emailId + '\'' +
                ", name='" + name + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                '}';
    }
}
