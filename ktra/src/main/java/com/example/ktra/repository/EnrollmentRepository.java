package com.example.ktra.repository;

import com.example.ktra.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByStudentStudentId(Long studentId);
    boolean existsByStudentStudentIdAndCourseId(Long studentId, Long courseId);
}
