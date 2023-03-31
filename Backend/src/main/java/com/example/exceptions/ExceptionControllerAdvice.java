package com.example.exceptions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDate;

@RestControllerAdvice
public class ExceptionControllerAdvice {
    private final Log LOG = LogFactory.getLog(Log.class);
    @Autowired
    private Environment environment;
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomErrorResponse> exceptionHandler(Exception exception) {

        LOG.debug(exception.getMessage(),exception);

        return new ResponseEntity<>(
                new CustomErrorResponse(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        environment.getProperty("General.EXCEPTION_MESSAGE"),
                        LocalDate.now().toString()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(StudentDashboardException.class)
    public ResponseEntity<CustomErrorResponse> studentDashboardExceptionHandler(StudentDashboardException exception){

        LOG.debug(exception.getMessage(),exception);

        HttpStatus httpStatus = org.springframework.http.HttpStatus.NOT_FOUND;

        if(exception.getMessage().contains("INCORRECT") || exception.getMessage().contains("INVALID") || exception.getMessage().contains("ALREADY_EXISTS")){
            httpStatus = HttpStatus.BAD_REQUEST;
        }

        return new ResponseEntity<>(
                new CustomErrorResponse(
                        httpStatus.value(),
                        environment.getProperty(exception.getMessage()),
                        LocalDate.now().toString()),
                httpStatus);
    }
}
