package com.example.ktra.service;

import com.example.ktra.entity.Category;
import com.example.ktra.entity.Course;
import com.example.ktra.repository.CategoryRepository;
import com.example.ktra.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public Page<Course> getCourses(int page, int size) {
        return courseRepository.findAll(PageRequest.of(page, size, Sort.by("id")));
    }

    public Page<Course> searchCourses(String keyword, int page, int size) {
        return courseRepository.findByNameContainingIgnoreCase(
                keyword, PageRequest.of(page, size, Sort.by("id")));
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll(Sort.by("name"));
    }

    public Course getCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy học phần: " + id));
    }

    public void saveCourse(Course course) {
        courseRepository.save(course);
    }

    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll(Sort.by("name"));
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }
}
