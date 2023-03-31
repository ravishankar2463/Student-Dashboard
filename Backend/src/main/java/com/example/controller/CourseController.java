package com.example.controller;

import com.example.exceptions.StudentDashboardException;
import com.example.models.Course;
import com.example.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @Autowired
    private Environment env;

    @GetMapping("/")
    public ResponseEntity<List<Course>> getAllCourses(){
        return new ResponseEntity<>(courseService.getAllCourses(), HttpStatus.OK);
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<Course> getCourse(@PathVariable int courseId) throws StudentDashboardException {
        return new ResponseEntity<>(courseService.getCourse(courseId),HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<String> createCourse(@RequestBody Course course) throws StudentDashboardException {
        courseService.createCourse(course);
        return new ResponseEntity<>(env.getProperty("Controller.COURSE_ADDED"),HttpStatus.OK);
    }

    @PutMapping("/")
    public ResponseEntity<String> updateCourse(@RequestBody Course course) throws StudentDashboardException {
        courseService.updateCourse(course);
        return new ResponseEntity<>(env.getProperty("Controller.COURSE_UPDATED"),HttpStatus.OK);
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<String> deleteStudent(@PathVariable int courseId) throws StudentDashboardException {
        courseService.deleteCourse(courseId);
        return new ResponseEntity<>(env.getProperty("Controller.COURSE_DELETED"),HttpStatus.OK);
    }
}
