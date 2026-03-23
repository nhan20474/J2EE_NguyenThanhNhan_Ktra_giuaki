package com.example.ktra.controller;

import com.example.ktra.entity.Course;
import com.example.ktra.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminCourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping("/courses")
    public String listCourses(Model model) {
        model.addAttribute("courses", courseService.getAllCourses());
        return "admin/courses";
    }

    @GetMapping("/courses/new")
    public String newCourseForm(Model model) {
        model.addAttribute("course", new Course());
        model.addAttribute("categories", courseService.getAllCategories());
        model.addAttribute("isNew", true);
        return "admin/course-form";
    }

    @PostMapping("/courses")
    public String createCourse(
            @RequestParam String name,
            @RequestParam(defaultValue = "") String image,
            @RequestParam int credits,
            @RequestParam String lecturer,
            @RequestParam(required = false) Long categoryId,
            RedirectAttributes redirectAttributes) {

        Course course = new Course();
        course.setName(name.trim());
        course.setImage(image.trim());
        course.setCredits(credits);
        course.setLecturer(lecturer.trim());
        if (categoryId != null) {
            course.setCategory(courseService.getCategoryById(categoryId));
        }
        courseService.saveCourse(course);
        redirectAttributes.addFlashAttribute("successMsg", "Đã thêm học phần \"" + name + "\" thành công!");
        return "redirect:/admin/courses";
    }

    @GetMapping("/courses/{id}/edit")
    public String editCourseForm(@PathVariable Long id, Model model) {
        model.addAttribute("course", courseService.getCourseById(id));
        model.addAttribute("categories", courseService.getAllCategories());
        model.addAttribute("isNew", false);
        return "admin/course-form";
    }

    @PostMapping("/courses/{id}")
    public String updateCourse(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam(defaultValue = "") String image,
            @RequestParam int credits,
            @RequestParam String lecturer,
            @RequestParam(required = false) Long categoryId,
            RedirectAttributes redirectAttributes) {

        Course course = courseService.getCourseById(id);
        course.setName(name.trim());
        course.setImage(image.trim());
        course.setCredits(credits);
        course.setLecturer(lecturer.trim());
        course.setCategory(categoryId != null ? courseService.getCategoryById(categoryId) : null);
        courseService.saveCourse(course);
        redirectAttributes.addFlashAttribute("successMsg", "Đã cập nhật học phần \"" + name + "\" thành công!");
        return "redirect:/admin/courses";
    }

    @PostMapping("/courses/{id}/delete")
    public String deleteCourse(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        String name = courseService.getCourseById(id).getName();
        courseService.deleteCourse(id);
        redirectAttributes.addFlashAttribute("successMsg", "Đã xóa học phần \"" + name + "\".");
        return "redirect:/admin/courses";
    }
}
