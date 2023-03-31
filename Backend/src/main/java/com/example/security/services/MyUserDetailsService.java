package com.example.security.services;

import com.example.exceptions.StudentDashboardException;
import com.example.models.LoginDetails;
import com.example.models.NewUser;
import com.example.models.Student;
import com.example.repository.StudentRepository;
import com.example.security.models.MyUserDetails;
import com.example.security.models.User;
import com.example.security.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Objects;
import java.util.Optional;
@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    StudentRepository studentRepository;

    @Value("${security.cookie.token.secret-key}")
    private String secretKey;

    public String createToken(LoginDetails loginDetails) {
        return loginDetails.getEmail() + "&" + loginDetails.getPassword() + "&" + calculateHmac(loginDetails);
    }

    private String calculateHmac(LoginDetails loginDetails) {
        byte[] secretKeyBytes = Objects.requireNonNull(secretKey).getBytes(StandardCharsets.UTF_8);
        byte[] valueBytes = Objects.requireNonNull(loginDetails.getEmail() + "&" + loginDetails.getPassword()).getBytes(StandardCharsets.UTF_8);

        try {
            Mac mac = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKeyBytes, "HmacSHA512");
            mac.init(secretKeySpec);
            byte[] hmacBytes = mac.doFinal(valueBytes);
            return Base64.getEncoder().encodeToString(hmacBytes);

        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findById(username);

        if(userOptional.isPresent()){
            return new MyUserDetails(userOptional.get());
        }else {
            throw new UsernameNotFoundException("No user found in database with username : "+username);
        }
    }

    public void createNewUser(NewUser newUser) throws StudentDashboardException {
        Optional<Student> studentOptional = studentRepository.findByEmailId(newUser.getEmail());

        LocalDate dob;

        try {
            dob = LocalDate.parse(newUser.getDateOfBirth());
        }catch (Exception e){
            throw new StudentDashboardException("Service.INCORRECT_DOB_FORMAT");
        }

        if(studentOptional.isEmpty()){
            Student student = new Student();
            student.setEmailId(newUser.getEmail());
            student.setName(newUser.getName());
            student.setDateOfBirth(dob);

            Student savedStudent = studentRepository.save(student);

            User user = new User();
            user.setEmailId(newUser.getEmail());
            user.setPassword(passwordEncoder().encode(newUser.getPassword()));
            user.setEnabled(true);
            user.setStudent(savedStudent);
            userRepository.save(user);

        }else {
            throw new StudentDashboardException("Service.USER_ALREADY_EXISTS");
        }
    }

    public User authenticate(LoginDetails loginDetails) throws StudentDashboardException {
        return findByLogin(loginDetails.getEmail(), loginDetails.getPassword(),true);
    }

    public User findByToken(String token) throws StudentDashboardException {
        String[] parts = token.split("&");

        String email = parts[0];
        String password = parts[1];
        String hmac = parts[2];

        User user = findByLogin(email,password,false);

        if (!hmac.equals(calculateHmac(new LoginDetails(user.getEmailId(), user.getPassword())))) {
            throw new StudentDashboardException("Service.INCORRECT_INVALID_COOKIE");
        }

        return user;
    }

    public User findByLogin(String email,String password,boolean matchPassword) throws StudentDashboardException {
        Optional<User> userOptional = userRepository.findById(email);

        if(userOptional.isPresent()){
            User user = userOptional.get();

            if(matchPassword) {
                if (!passwordEncoder().matches(password, user.getPassword())) {
                    throw new StudentDashboardException("Service.INCORRECT_LOGIN_DETAILS");
                }
            }

            return user;
        }else {
            throw new StudentDashboardException("Service.STUDENT_NOT_FOUND");
        }


    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
