package com.example.ktra.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "course")
@Getter @Setter @NoArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String image;

    private int credits;

    private String lecturer;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
