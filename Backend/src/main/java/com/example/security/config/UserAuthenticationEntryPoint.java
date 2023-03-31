package com.example.security.config;

import com.example.exceptions.CustomErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
@Component
public class UserAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private Log LOG = LogFactory.getLog(Log.class);

    private final ObjectMapper OBJECT_MAPPER;

    public UserAuthenticationEntryPoint(ObjectMapper objectMapper) {
        OBJECT_MAPPER = objectMapper;
    }


    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        LOG.error(authException.getMessage(),authException);

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        OBJECT_MAPPER.writeValue(response.getOutputStream(), new CustomErrorResponse(HttpStatus.UNAUTHORIZED.value(),"Unauthorized path", LocalDate.now().toString()));
    }
}
