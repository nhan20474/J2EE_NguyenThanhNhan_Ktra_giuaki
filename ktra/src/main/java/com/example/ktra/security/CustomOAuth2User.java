package com.example.ktra.security;

import com.example.ktra.entity.Student;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public class CustomOAuth2User implements OAuth2User {

    private final OAuth2User oAuth2User;
    private final Student student;

    public CustomOAuth2User(OAuth2User oAuth2User, Student student) {
        this.oAuth2User = oAuth2User;
        this.student = student;
    }

    public Student getStudent() {
        return student;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return student.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getName() {
        return student.getUsername();
    }
}
