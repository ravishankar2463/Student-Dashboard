package com.example.repository;

import com.example.models.Student;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface StudentRepository extends CrudRepository<Student, Integer> {
    Optional<Student> findByEmailId(String emailId);
}
