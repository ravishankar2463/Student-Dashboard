package com.example.controller;

import com.example.exceptions.StudentDashboardException;
import com.example.models.Course;
import com.example.models.CourseUpdate;
import com.example.models.Student;
import com.example.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private Environment env;

    @GetMapping("/")
    public ResponseEntity<List<Student>> getAllStudents(){
        return new ResponseEntity<>(studentService.getAllStudents(), HttpStatus.OK);
    }

    @GetMapping("/{identifier}")
    public ResponseEntity<Student> getStudent(@PathVariable String identifier,@RequestParam(required = false) String email) throws StudentDashboardException {
        if(email != null){
            if("Y".equals(email)){
                return new ResponseEntity<>(studentService.getStudentByEmail(identifier),HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(studentService.getStudent(Integer.parseInt(identifier)),HttpStatus.OK);
    }
    @PostMapping("/")
    public ResponseEntity<String> createStudent(@RequestBody Student student) throws StudentDashboardException {
        studentService.createStudent(student);
        return new ResponseEntity<>(env.getProperty("Controller.STUDENT_ADDED"),HttpStatus.OK);
    }

    @PutMapping("/")
    public ResponseEntity<String> updateStudent(@RequestBody Student student) throws StudentDashboardException {
        studentService.updateStudent(student);
        return new ResponseEntity<>(env.getProperty("Controller.STUDENT_UPDATED"),HttpStatus.OK);
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity<String> deleteStudent(@PathVariable int studentId) throws StudentDashboardException {
        studentService.deleteStudent(studentId);
        return new ResponseEntity<>(env.getProperty("Controller.STUDENT_DELETED"),HttpStatus.OK);
    }

    @GetMapping("/{studentId}/courses")
    public ResponseEntity<Set<Course>> getStudentEnrolledCourses(@PathVariable int studentId) throws StudentDashboardException {
        return new ResponseEntity<>(studentService.getStudentEnrolledCourses(studentId), HttpStatus.OK);
    }

    @PostMapping("/{studentId}/courses")
    public ResponseEntity<String> addOrRemoveCourseFromStudentEnrolledCourses(@PathVariable int studentId,
                                                                              @RequestBody CourseUpdate courseUpdate) throws StudentDashboardException {
        if(CourseUpdate.CourseUpdateActions.ADD == courseUpdate.getAction()) {
            studentService.addCourseToTheStudentsEnrolledCourses(studentId,Integer.parseInt(courseUpdate.getCourseId()));
            return new ResponseEntity<>(env.getProperty("Controller.STUDENT_COURSE_ADDED"), HttpStatus.OK);
        } else if (CourseUpdate.CourseUpdateActions.REMOVE == courseUpdate.getAction()) {
            studentService.removeCourseFromTheStudentsEnrolledCourses(studentId,Integer.parseInt(courseUpdate.getCourseId()));
            return new ResponseEntity<>(env.getProperty("Controller.STUDENT_COURSE_REMOVED"), HttpStatus.OK);
        }else {
            throw new StudentDashboardException("Controller.STUDENT_INVALID_ACTION");
        }
    }
}
