package com.example.controller;

import com.example.exceptions.StudentDashboardException;
import com.example.models.NewUser;
import com.example.security.models.User;
import com.example.security.services.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class AuthenticationController {

    @Autowired
    MyUserDetailsService userDetailsService;

    @Autowired
    Environment env;

    @PostMapping("sign-up")
    public ResponseEntity<String> createNewUser(@RequestBody NewUser newUser) throws StudentDashboardException {
        userDetailsService.createNewUser(newUser);
        return new ResponseEntity<>(env.getProperty("Controller.USER_ADDED"),HttpStatus.OK);
    }

    @PostMapping("sign-out")
    public ResponseEntity<String> signOut(@AuthenticationPrincipal User user) {
        SecurityContextHolder.clearContext();
        return new ResponseEntity<>(env.getProperty("Controller.SUCCESSFUL_LOGOUT"),HttpStatus.OK);
    }

}
