package com.example.repository;

import com.example.models.Course;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CourseRepository extends CrudRepository<Course, Integer> {
}
