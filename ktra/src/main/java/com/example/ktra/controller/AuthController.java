package com.example.ktra.controller;

import com.example.ktra.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/login")
    public String loginPage(
            @RequestParam(required = false) String error,
            @RequestParam(required = false) String logout,
            @RequestParam(required = false) String registered,
            Model model) {

        if (error != null) {
            model.addAttribute("errorMsg", "Tên đăng nhập hoặc mật khẩu không đúng!");
        }
        if (logout != null) {
            model.addAttribute("successMsg", "Bạn đã đăng xuất thành công.");
        }
        if (registered != null) {
            model.addAttribute("successMsg", "Đăng ký thành công! Vui lòng đăng nhập.");
        }
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        return "register";
    }

    @PostMapping("/register")
    public String register(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String email,
            Model model) {

        if (username == null || username.trim().length() < 3) {
            model.addAttribute("errorMsg", "Tên đăng nhập phải có ít nhất 3 ký tự.");
            return "register";
        }
        if (password == null || password.length() < 6) {
            model.addAttribute("errorMsg", "Mật khẩu phải có ít nhất 6 ký tự.");
            return "register";
        }
        if (studentService.existsByUsername(username.trim())) {
            model.addAttribute("errorMsg", "Tên đăng nhập đã được sử dụng.");
            return "register";
        }
        if (studentService.existsByEmail(email.trim())) {
            model.addAttribute("errorMsg", "Email đã được đăng ký.");
            return "register";
        }

        studentService.registerStudent(username, password, email);
        return "redirect:/login?registered=true";
    }
}
