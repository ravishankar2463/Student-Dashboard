package com.example.service;

import com.example.exceptions.StudentDashboardException;
import com.example.models.Course;
import com.example.models.Student;

import java.util.List;
import java.util.Set;

public interface StudentService {
    List<Student> getAllStudents();
    Student getStudent(int id) throws StudentDashboardException;
    void createStudent(Student student) throws StudentDashboardException;
    void updateStudent(Student student) throws StudentDashboardException;
    void deleteStudent(int id) throws StudentDashboardException;
    Set<Course> getStudentEnrolledCourses(int id) throws StudentDashboardException;
    Student getStudentByEmail(String email) throws StudentDashboardException;
    void addCourseToTheStudentsEnrolledCourses(int studentId,int courseId) throws StudentDashboardException;
    void removeCourseFromTheStudentsEnrolledCourses(int studentId,int courseId) throws StudentDashboardException;
}
