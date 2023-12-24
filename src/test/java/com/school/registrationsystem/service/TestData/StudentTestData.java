package com.school.registrationsystem.service.TestData;

import com.school.registrationsystem.model.Student;
import com.school.registrationsystem.model.dto.StudentDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentTestData {
    public static List<Student> testStudentList() {
        return List.of(Student.builder()
                        .name("name")
                        .surname("surname")
                        .studentIndex(1234)
                        .build(),
                Student.builder()
                        .name("name")
                        .surname("surname")
                        .studentIndex(1235)
                        .build()

        );
    }

    public static Student testStudent() {
        return Student.builder()
                .name("name")
                .surname("surname")
                .studentIndex(1234)
                .build();
    }

    public StudentDto testExpectedStudentDto() {
        return StudentDto.builder()
                .name("newName")
                .studentIndex(1234)
                .surname("newSurname")
                .build();
    }
}