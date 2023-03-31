package com.example.security.repositories;

import com.example.security.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User,String> {
}
