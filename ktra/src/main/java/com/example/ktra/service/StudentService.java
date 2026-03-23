package com.example.ktra.service;

import com.example.ktra.entity.Role;
import com.example.ktra.entity.Student;
import com.example.ktra.repository.RoleRepository;
import com.example.ktra.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean existsByUsername(String username) {
        return studentRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return studentRepository.existsByEmail(email);
    }

    public void registerStudent(String username, String password, String email) {
        Role studentRole = roleRepository.findByName("STUDENT")
                .orElseThrow(() -> new RuntimeException("Role STUDENT không tồn tại"));

        Student student = new Student();
        student.setUsername(username.trim());
        student.setPassword(passwordEncoder.encode(password));
        student.setEmail(email.trim().toLowerCase());
        student.setRoles(new HashSet<>(Set.of(studentRole)));

        studentRepository.save(student);
    }
}
