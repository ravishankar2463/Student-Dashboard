package com.example.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

public class UsernamePasswordAuthFilter extends UsernamePasswordAuthenticationFilter {
    private static final String BODY_ATTRIBUTE = UsernamePasswordAuthFilter.class.getSimpleName() + ".body";
    private final ObjectMapper OBJECT_MAPPER;

    private Log LOG = LogFactory.getLog(Log.class);

    public UsernamePasswordAuthFilter(ObjectMapper objectMapper) {
        OBJECT_MAPPER = objectMapper;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        if ("/login".equals(request.getServletPath())
                && HttpMethod.POST.matches(request.getMethod())) {
            UsernamePasswordRequest usernamePasswordRequest = OBJECT_MAPPER.readValue(request.getInputStream(), UsernamePasswordRequest.class);

            LOG.debug(String.format("Login request : %n Email : %s %n Password : %s",
                    usernamePasswordRequest.get("email"),
                    usernamePasswordRequest.get("password")));

            request.setAttribute(BODY_ATTRIBUTE, usernamePasswordRequest);
        }

        super.doFilter(request, response, chain);
    }

    protected String obtainUsername(HttpServletRequest request) {
        UsernamePasswordRequest usernamePasswordRequest = (UsernamePasswordRequest) request.getAttribute(BODY_ATTRIBUTE);
        return usernamePasswordRequest.get(getUsernameParameter());
    }

    protected String obtainPassword(HttpServletRequest request) {
        UsernamePasswordRequest usernamePasswordRequest = (UsernamePasswordRequest) request.getAttribute(BODY_ATTRIBUTE);
        return usernamePasswordRequest.get(getPasswordParameter());
    }

    private static class UsernamePasswordRequest extends HashMap<String, String> {
        // Nothing, just a type marker
    }
}
