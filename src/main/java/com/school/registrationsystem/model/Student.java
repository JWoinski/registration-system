package com.school.registrationsystem.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int studentId;
    private String name;
    private String surname;
    @ToString.Exclude
    @ManyToMany(mappedBy = "studentList", cascade = {CascadeType.DETACH, CascadeType.PERSIST}, fetch = FetchType.EAGER)
    private List<Course> courseList;

    public Student(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }
}