package com.school.registrationsystem.service.TestData;

import com.school.registrationsystem.model.Student;
import com.school.registrationsystem.model.dto.StudentDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentTestData {
    private static int iter = 100;

    private static int getTestIndex() {
        iter++;
        return iter;
    }

    public static List<Student> testStudentList() {
        return List.of(Student.builder()
                        .name("name")
                        .surname("surname")
                        .studentIndex(getTestIndex())
                        .build(),
                Student.builder()
                        .name("name")
                        .surname("surname")
                        .studentIndex(getTestIndex())
                        .build()

        );
    }

    public static Student testStudent() {
        return Student.builder()
                .name("name")
                .surname("surname")
                .studentIndex(getTestIndex())
                .build();
    }

    public StudentDto testExpectedStudentDto(int index) {
        return StudentDto.builder()
                .name("newName")
                .studentIndex(index)
                .surname("newSurname")
                .build();
    }
}