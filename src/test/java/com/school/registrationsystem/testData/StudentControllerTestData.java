package com.school.registrationsystem.testData;

import com.school.registrationsystem.model.Student;
import com.school.registrationsystem.model.dto.StudentDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

@Service
public class StudentControllerTestData {
    public Student testStudent_correctValues() {
        return Student.builder()
                .name("nazwa studenta")
                .surname("nazwisko studenta")
                .build();
    }

    public Student testStudent_NullValues() {
        return new Student();
    }

    public Student testStudent_correctValues1() {
        return Student.builder()
                .name("nazwa studenta 1")
                .surname("nazwisko studenta 1")
                .build();
    }

    public Student testStudent_correctValues2() {
        return Student.builder()
                .name("nazwa studenta 2")
                .surname("nazwisko studenta 2")
                .build();
    }

    public StudentDto testStudentDto_correctValues() {
        return StudentDto.builder()
                .name("nazwa studenta")
                .surname("nazwisko studenta")
                .build();
    }

    public StudentDto testStudentDto_modifiedValues() {
        return StudentDto.builder()
                .name("nowe imie studenta")
                .surname("nowe nazwisko studenta")
                .build();
    }

    public List<Student> testList50Students() {
        return IntStream.range(0, 50)
                .mapToObj(i -> Student.builder()
                        .studentId(i + 1)
                        .name("name")
                        .surname("surname")
                        .build())
                .toList();
    }
}