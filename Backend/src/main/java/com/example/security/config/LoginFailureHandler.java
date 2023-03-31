package com.example.security.config;

import com.example.exceptions.CustomErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class LoginFailureHandler implements AuthenticationFailureHandler {

    private final ObjectMapper OBJECT_MAPPER;

    @Autowired
    Environment env;

    public LoginFailureHandler(ObjectMapper objectMapper) {
        this.OBJECT_MAPPER = objectMapper;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        OBJECT_MAPPER.writeValue(response.getWriter(),
                new CustomErrorResponse(HttpStatus.BAD_REQUEST.value(), env.getProperty("Service.INCORRECT_LOGIN_DETAILS"), LocalDate.now().toString()));
    }
}
