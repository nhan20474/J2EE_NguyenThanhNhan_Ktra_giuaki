package com.example.ktra.service;

import com.example.ktra.entity.Role;
import com.example.ktra.entity.Student;
import com.example.ktra.repository.RoleRepository;
import com.example.ktra.repository.StudentRepository;
import com.example.ktra.security.CustomOAuth2User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");

        Student student = studentRepository.findByEmail(email).orElseGet(() -> {
            String baseUsername = email.split("@")[0];
            String username = baseUsername;
            int suffix = 1;
            while (studentRepository.existsByUsername(username)) {
                username = baseUsername + suffix++;
            }

            Student newStudent = new Student();
            newStudent.setUsername(username);
            newStudent.setEmail(email);
            newStudent.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));

            Role studentRole = roleRepository.findByName("STUDENT")
                    .orElseThrow(() -> new RuntimeException("Role STUDENT khong ton tai"));
            newStudent.setRoles(new HashSet<>(Set.of(studentRole)));

            return studentRepository.save(newStudent);
        });

        return new CustomOAuth2User(oAuth2User, student);
    }
}
