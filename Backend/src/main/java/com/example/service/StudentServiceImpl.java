package com.example.service;

import com.example.exceptions.StudentDashboardException;
import com.example.models.Course;
import com.example.models.Student;
import com.example.repository.CourseRepository;
import com.example.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService{
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Override
    public List<Student> getAllStudents() {
        List<Student> studentList = new ArrayList<>();
        studentRepository.findAll().forEach(studentList::add);
        return studentList;
    }

    @Override
    public Student getStudent(int id) throws StudentDashboardException {
        return studentRepository.findById(id)
                .orElseThrow(() -> new StudentDashboardException("Service.STUDENT_NOT_FOUND"));
    }

    @Override
    public Student getStudentByEmail(String emailId) throws StudentDashboardException {
        return studentRepository.findByEmailId(emailId)
                .orElseThrow(() -> new StudentDashboardException("Service.STUDENT_NOT_FOUND"));
    }

    @Override
    public void createStudent(Student student) throws StudentDashboardException {
        if(student.getId() != null) {
            Optional<Student> studentOptional = studentRepository.findById(student.getId());
            if(studentOptional.isPresent()){
                throw new StudentDashboardException("Service.STUDENT_ALREADY_EXISTS");
            }else {
                studentRepository.save(student);
            }
        }else {
            studentRepository.save(student);
        }
    }

    @Override
    public void updateStudent(Student student) throws StudentDashboardException {
        Student s = studentRepository.findById(student.getId())
                .orElseThrow(() -> new StudentDashboardException("Service.STUDENT_NOT_FOUND"));

        if(!student.getName().isEmpty()) {s.setName(student.getName());}

        if(!student.getEmailId().isEmpty()) {s.setEmailId(student.getEmailId());}

        studentRepository.save(s);
    }

    @Override
    public void deleteStudent(int id) throws StudentDashboardException {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentDashboardException("Service.STUDENT_NOT_FOUND"));

        studentRepository.delete(student);
    }

    @Override
    public Set<Course> getStudentEnrolledCourses(int id) throws StudentDashboardException {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentDashboardException("Service.STUDENT_NOT_FOUND"));

        return student.getSubscribedCourses();
    }

    @Override
    public void addCourseToTheStudentsEnrolledCourses(int studentId, int courseId) throws StudentDashboardException {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new StudentDashboardException("Service.COURSE_NOT_FOUND"));
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new StudentDashboardException("Service.STUDENT_NOT_FOUND"));

        Set<Course> enrolledCourses = student.getSubscribedCourses();

        Collection<Course> filteredCourses =  enrolledCourses.stream().filter((c) -> c.getId().equals(course.getId())).collect(Collectors.toSet());

        if(filteredCourses.isEmpty()){
            enrolledCourses.add(course);
            student.setSubscribedCourses(enrolledCourses);
            studentRepository.save(student);
        }else {
            throw new StudentDashboardException("Service.COURSE_ALREADY_ENLISTED");
        }
    }

    @Override
    public void removeCourseFromTheStudentsEnrolledCourses(int studentId, int courseId) throws StudentDashboardException{
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new StudentDashboardException("Service.COURSE_NOT_FOUND"));
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new StudentDashboardException("Service.STUDENT_NOT_FOUND"));

        Set<Course> enrolledCourses = student.getSubscribedCourses();

        Collection<Course> filteredCourses =  enrolledCourses.stream().filter((c) -> c.getId().equals(course.getId())).collect(Collectors.toSet());

        if(!filteredCourses.isEmpty()){
            enrolledCourses.remove(course);
            student.setSubscribedCourses(enrolledCourses);
            studentRepository.save(student);
        }else {
            throw new StudentDashboardException("Service.COURSE_NOT_FOUND_IN_ENLISTED_COURSES");
        }
    }
}
