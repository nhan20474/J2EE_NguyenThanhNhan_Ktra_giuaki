package com.example.ktra.service;

import com.example.ktra.entity.Course;
import com.example.ktra.entity.Enrollment;
import com.example.ktra.entity.Student;
import com.example.ktra.repository.CourseRepository;
import com.example.ktra.repository.EnrollmentRepository;
import com.example.ktra.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    public boolean isEnrolled(Long studentId, Long courseId) {
        return enrollmentRepository.existsByStudentStudentIdAndCourseId(studentId, courseId);
    }

    public String enroll(Long studentId, Long courseId) {
        if (isEnrolled(studentId, courseId)) {
            return "already_enrolled";
        }

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sinh viên"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy học phần"));

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setEnrollDate(LocalDate.now());
        enrollmentRepository.save(enrollment);

        return "success";
    }

    public List<Enrollment> getEnrollmentsByStudentId(Long studentId) {
        return enrollmentRepository.findByStudentStudentId(studentId);
    }

    public Set<Long> getEnrolledCourseIds(Long studentId) {
        return enrollmentRepository.findByStudentStudentId(studentId).stream()
                .map(e -> e.getCourse().getId())
                .collect(Collectors.toSet());
    }
}
