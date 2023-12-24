package com.school.registrationsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int studentId;
    private String name;
    private String surname;
    private int studentIndex;
    @ManyToMany(mappedBy = "studentList", cascade = {CascadeType.DETACH, CascadeType.PERSIST}, fetch = FetchType.EAGER)
    private List<Course> courseList;

    public Student(String name, String surname, int studentIndex) {
        this.name = name;
        this.surname = surname;
        this.studentIndex = studentIndex;
    }
}