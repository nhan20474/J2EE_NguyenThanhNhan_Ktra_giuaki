package com.example.ktra.controller;

import com.example.ktra.entity.Enrollment;
import com.example.ktra.security.CustomOAuth2User;
import com.example.ktra.security.CustomUserDetails;
import com.example.ktra.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/enroll")
public class EnrollController {

    @Autowired
    private EnrollmentService enrollmentService;

    @PostMapping("/{courseId}")
    public String enroll(
            @PathVariable Long courseId,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        Long studentId = extractStudentId(authentication);
        if (studentId == null) return "redirect:/login";

        String result = enrollmentService.enroll(studentId, courseId);
        if ("already_enrolled".equals(result)) {
            redirectAttributes.addFlashAttribute("warningMsg", "Bạn đã đăng ký học phần này rồi!");
        } else {
            redirectAttributes.addFlashAttribute("successMsg", "Đăng ký học phần thành công!");
        }
        return "redirect:/home";
    }

    @GetMapping("/my-courses")
    public String myCourses(Authentication authentication, Model model) {
        Long studentId = extractStudentId(authentication);
        if (studentId == null) return "redirect:/login";

        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByStudentId(studentId);
        model.addAttribute("enrollments", enrollments);
        return "enroll/my-courses";
    }

    private Long extractStudentId(Authentication auth) {
        if (auth == null) return null;
        if (auth.getPrincipal() instanceof CustomUserDetails ud) {
            return ud.getStudent().getStudentId();
        } else if (auth.getPrincipal() instanceof CustomOAuth2User ou) {
            return ou.getStudent().getStudentId();
        }
        return null;
    }
}
