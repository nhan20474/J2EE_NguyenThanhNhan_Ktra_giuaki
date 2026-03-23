package com.example.ktra.config;

import com.example.ktra.entity.*;
import com.example.ktra.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired private RoleRepository roleRepository;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private StudentRepository studentRepository;
    @Autowired private CourseRepository courseRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Tao roles
        Role adminRole = roleRepository.findByName("ADMIN").orElseGet(() -> {
            Role r = new Role(); r.setName("ADMIN"); return roleRepository.save(r);
        });
        Role studentRole = roleRepository.findByName("STUDENT").orElseGet(() -> {
            Role r = new Role(); r.setName("STUDENT"); return roleRepository.save(r);
        });

        // Tao tai khoan admin mac dinh
        if (studentRepository.findByUsername("admin").isEmpty()) {
            Student admin = new Student();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("admin@coursehub.com");
            admin.setRoles(new HashSet<>(Set.of(adminRole)));
            studentRepository.save(admin);
            System.out.println(">>> Da tao tai khoan admin (username: admin / password: admin123)");
        }

        // Tao tai khoan student demo
        if (studentRepository.findByUsername("student1").isEmpty()) {
            Student s = new Student();
            s.setUsername("student1");
            s.setPassword(passwordEncoder.encode("student123"));
            s.setEmail("student1@coursehub.com");
            s.setRoles(new HashSet<>(Set.of(studentRole)));
            studentRepository.save(s);
        }

        // Tao danh muc
        if (categoryRepository.count() == 0) {
            for (String name : List.of(
                    "Công nghệ thông tin", "Khoa học tự nhiên",
                    "Toán học", "Kinh tế", "Ngoại ngữ")) {
                Category c = new Category();
                c.setName(name);
                categoryRepository.save(c);
            }
        }

        // Tao hoc phan mau
        if (courseRepository.count() == 0) {
            List<Category> categories = categoryRepository.findAll();
            Category cntt = categories.get(0);
            Category khtn = categories.size() > 1 ? categories.get(1) : cntt;
            Category math = categories.size() > 2 ? categories.get(2) : cntt;

            Object[][] courses = {
                {"Lập trình Java", 3, "Nguyễn Văn An", cntt,
                 "https://images.unsplash.com/photo-1517694712202-14dd9538aa97?w=400&h=250&fit=crop"},
                {"Cơ sở dữ liệu", 3, "Trần Thị Bình", cntt,
                 "https://images.unsplash.com/photo-1544383835-bda2bc66a55d?w=400&h=250&fit=crop"},
                {"Mạng máy tính", 3, "Lê Văn Cường", cntt,
                 "https://images.unsplash.com/photo-1558494949-ef010cbdcc31?w=400&h=250&fit=crop"},
                {"Trí tuệ nhân tạo", 4, "Phạm Thị Dung", cntt,
                 "https://images.unsplash.com/photo-1677442135703-1787eea5ce01?w=400&h=250&fit=crop"},
                {"Lập trình Web", 3, "Hoàng Văn Đức", cntt,
                 "https://images.unsplash.com/photo-1547658719-da2b51169166?w=400&h=250&fit=crop"},
                {"Giải tích 1", 4, "Nguyễn Thị Phương", math,
                 "https://images.unsplash.com/photo-1635070041078-e363dbe005cb?w=400&h=250&fit=crop"},
                {"Đại số tuyến tính", 3, "Trần Văn Giang", math,
                 "https://images.unsplash.com/photo-1509228468518-180dd4864904?w=400&h=250&fit=crop"},
                {"Vật lý đại cương", 3, "Lê Thị Hoa", khtn,
                 "https://images.unsplash.com/photo-1636466497217-26a8cbeaf0aa?w=400&h=250&fit=crop"},
                {"Hóa học đại cương", 3, "Phạm Văn Hùng", khtn,
                 "https://images.unsplash.com/photo-1532187863486-abf9dbad1b69?w=400&h=250&fit=crop"},
                {"Spring Boot Framework", 3, "Nguyễn Thanh Nhân", cntt,
                 "https://images.unsplash.com/photo-1555099962-4199c345e5dd?w=400&h=250&fit=crop"},
            };

            for (Object[] data : courses) {
                Course c = new Course();
                c.setName((String) data[0]);
                c.setCredits((Integer) data[1]);
                c.setLecturer((String) data[2]);
                c.setCategory((Category) data[3]);
                c.setImage((String) data[4]);
                courseRepository.save(c);
            }
            System.out.println(">>> Da tao " + courses.length + " hoc phan mau");
        }
    }
}
