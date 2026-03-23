package com.example.ktra.controller;

import com.example.ktra.entity.Course;
import com.example.ktra.security.CustomUserDetails;
import com.example.ktra.service.CourseService;
import com.example.ktra.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.Set;

@Controller
public class HomeController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private EnrollmentService enrollmentService;

    @GetMapping("/")
    public String index() {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String home(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "0") int page,
            Model model,
            Authentication authentication) {

        final int PAGE_SIZE = 5;
        Page<Course> coursePage;

        if (search != null && !search.trim().isEmpty()) {
            coursePage = courseService.searchCourses(search.trim(), page, PAGE_SIZE);
        } else {
            coursePage = courseService.getCourses(page, PAGE_SIZE);
        }

        model.addAttribute("courses", coursePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", coursePage.getTotalPages());
        model.addAttribute("totalItems", coursePage.getTotalElements());
        model.addAttribute("search", search);

        // Lay danh sach hoc phan da dang ky cua sinh vien hien tai
        Set<Long> enrolledIds = Collections.emptySet();
        if (authentication != null && authentication.isAuthenticated()) {
            Long studentId = extractStudentId(authentication);
            if (studentId != null) {
                enrolledIds = enrollmentService.getEnrolledCourseIds(studentId);
            }
        }
        model.addAttribute("enrolledIds", enrolledIds);

        return "home";
    }

    private Long extractStudentId(Authentication auth) {
        if (auth.getPrincipal() instanceof CustomUserDetails ud) {
            return ud.getStudent().getStudentId();
        }
        return null;
    }
}
