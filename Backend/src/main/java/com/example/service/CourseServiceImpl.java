package com.example.service;

import com.example.exceptions.StudentDashboardException;
import com.example.models.Course;
import com.example.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class CourseServiceImpl implements CourseService{
    @Autowired
    private CourseRepository courseRepository;
    @Override
    public List<Course> getAllCourses() {
        List<Course> allCourseList = new ArrayList<>();
        courseRepository.findAll().forEach((allCourseList::add));
        return allCourseList;
    }

    @Override
    public Course getCourse(int id) throws StudentDashboardException {
        return courseRepository.findById(id)
                .orElseThrow(() -> new StudentDashboardException("Service.COURSE_NOT_FOUND"));
    }

    @Override
    public void createCourse(Course course) throws StudentDashboardException {
        if(course.getId() != null) {
            Optional<Course> studentOptional = courseRepository.findById(course.getId());
            if(studentOptional.isPresent()){
                throw new StudentDashboardException("Service.COURSE_ALREADY_EXISTS");
            }else {
                courseRepository.save(course);
            }
        }else {
            courseRepository.save(course);
        }
    }

    @Override
    public void updateCourse(Course course) throws StudentDashboardException {
        Course c = courseRepository.findById(course.getId())
                .orElseThrow(() -> new StudentDashboardException("Service.COURSE_NOT_FOUND"));

        if(!course.getName().isEmpty()) {c.setName(course.getName());}

        if(!course.getDescription().isEmpty()) {c.setDescription(course.getDescription());}

        courseRepository.save(c);
    }

    @Override
    public void deleteCourse(int id) throws StudentDashboardException {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new StudentDashboardException("Service.COURSE_NOT_FOUND"));

        courseRepository.delete(course);
    }
}
