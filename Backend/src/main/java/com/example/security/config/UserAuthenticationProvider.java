package com.example.security.config;

import com.example.exceptions.CustomAuthenticationException;
import com.example.models.LoginDetails;
import com.example.security.models.User;
import com.example.security.services.MyUserDetailsService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class UserAuthenticationProvider implements AuthenticationProvider {

    private final MyUserDetailsService authenticationService;

    @Autowired
    Environment env;

    private final Log LOG = LogFactory.getLog(Logger.class);

    public UserAuthenticationProvider(MyUserDetailsService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        User user = null;

        try {
            if (authentication instanceof UsernamePasswordAuthenticationToken) {
                // authentication by username and password
                user = authenticationService.authenticate(
                        new LoginDetails((String) authentication.getPrincipal(), (String) authentication.getCredentials()));
            } else if (authentication instanceof PreAuthenticatedAuthenticationToken) {
                // authentication by cookie
                user = authenticationService.findByToken((String) authentication.getPrincipal());
            }
        }catch (Exception e){
            LOG.error(e.getMessage());
            throw new CustomAuthenticationException(env.getProperty(e.getMessage()));
        }


        if (user == null) {
            return null;
        }

        return new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
