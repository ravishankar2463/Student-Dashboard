package com.example.security.config;

import com.example.models.LoginDetails;
import com.example.security.models.MyUserDetails;
import com.example.security.models.User;
import com.example.security.services.MyUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final ObjectMapper OBJECT_MAPPER;

    private final Log LOG = LogFactory.getLog(Logger.class);

    @Autowired
    Environment env;

    @Autowired
    MyUserDetailsService userDetailsService;

    public LoginSuccessHandler(ObjectMapper objectMapper) {
        this.OBJECT_MAPPER = objectMapper;
    }


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            User user = (User) authentication.getPrincipal();

            String userCookieToken = userDetailsService.createToken(new LoginDetails(user.getEmailId(),user.getPassword()));

            LOG.info(String.format("Cookie Token Created for User %s : %s",user.getEmailId() ,userCookieToken));

            String profile = env.getProperty("spring.profiles.active");

            Cookie authCookie = new Cookie(CookieAuthenticationFilter.COOKIE_NAME, userCookieToken);

            authCookie.setHttpOnly(true);
            authCookie.setSecure(!"dev".equals(profile));
            authCookie.setMaxAge((int) Duration.of(1, ChronoUnit.DAYS).toSeconds());
            authCookie.setPath("/");

            response.addCookie(authCookie);

            response.setStatus(HttpStatus.OK.value());
            response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

            Map<String,String> responseMap = new HashMap<>();
            responseMap.put("email",user.getEmailId());
            responseMap.put("name",user.getStudent().getName());
            responseMap.put("id",String.valueOf(user.getStudent().getId()));

            OBJECT_MAPPER.writeValue(response.getWriter(),responseMap);
        }
    }
}
