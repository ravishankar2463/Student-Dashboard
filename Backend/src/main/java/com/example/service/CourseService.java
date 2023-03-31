package com.example.service;

import com.example.exceptions.StudentDashboardException;
import com.example.models.Course;

import java.util.List;

public interface CourseService {
    List<Course> getAllCourses();
    Course getCourse(int id) throws StudentDashboardException;
    void createCourse(Course course) throws StudentDashboardException;
    void updateCourse(Course course) throws StudentDashboardException;
    void deleteCourse(int id) throws StudentDashboardException;
}
