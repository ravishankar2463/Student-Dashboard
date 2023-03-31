package com.example.security.config;

import com.example.security.services.MyUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;
import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    @Autowired
    DataSource dataSource;

    @Autowired
    Environment env;

    @Autowired
    MyUserDetailsService userDetailsService;

    @Autowired
    UserAuthenticationProvider userAuthenticationProvider;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(userAuthenticationProvider)
                .userDetailsService(userDetailsService).and()
                .jdbcAuthentication().dataSource(dataSource)
                .usersByUsernameQuery("select email_id,password,enabled from users where email_id=?");
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.exceptionHandling().authenticationEntryPoint(getUserAuthenticationEntryPoint()).and()
                .csrf().disable()
                .cors().configurationSource(corsConfigurationSource()).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers("/students/**").authenticated()
                .antMatchers("/courses/**").authenticated()
                .and()
                .authorizeRequests().anyRequest().permitAll();

        AuthenticationConfiguration authenticationConfiguration = http.getSharedObject(AuthenticationConfiguration.class);

        http.addFilterBefore(getUsernamePasswordAuthFilter(authenticationManager(authenticationConfiguration)), UsernamePasswordAuthenticationFilter.class);
        http.addFilterAfter(getCookieAuthenticationFilter(), LogoutFilter.class);

        return http.build();
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public Filter getCookieAuthenticationFilter(){
        return new CookieAuthenticationFilter();
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public Filter getUsernamePasswordAuthFilter(AuthenticationManager authenticationManager) {
        UsernamePasswordAuthenticationFilter authenticationFilter = new UsernamePasswordAuthFilter(objectMapper());
        authenticationFilter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/login", "POST"));
        authenticationFilter.setUsernameParameter("email");
        authenticationFilter.setPasswordParameter("password");
        authenticationFilter.setAuthenticationManager(authenticationManager);
        authenticationFilter.setAuthenticationSuccessHandler(getLoginSuccessHandler());
        authenticationFilter.setAuthenticationFailureHandler(getLoginFailureHandler());

        return authenticationFilter;
    }

    @Bean
    public LoginSuccessHandler getLoginSuccessHandler(){
        return new LoginSuccessHandler(objectMapper());
    }

    @Bean
    public LoginFailureHandler getLoginFailureHandler(){
        return new LoginFailureHandler(objectMapper());
    }

    @Bean
    public AuthenticationEntryPoint getUserAuthenticationEntryPoint() {
        return new UserAuthenticationEntryPoint(objectMapper());
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        String profile = env.getProperty("spring.profiles.active");
        CorsConfiguration configuration = new CorsConfiguration();
        if("dev".equals(profile)) {
            configuration.setAllowedOrigins(List.of("http://localhost:5173"));
            configuration.addAllowedOrigin("http://localhost:5173");
        }else {
            configuration.setAllowedOrigins(List.of(Objects.requireNonNull(env.getProperty("cors.frontend.url"))));
            configuration.addAllowedOrigin(env.getProperty("cors.frontend.url"));
        }
        configuration.setAllowCredentials(true);
        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "PUT", "POST", "DELETE", "PATCH"));
        //the below three lines will add the relevant CORS response headers
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
