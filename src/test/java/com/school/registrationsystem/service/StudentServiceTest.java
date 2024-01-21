package com.school.registrationsystem.service;

import com.school.registrationsystem.model.Student;
import com.school.registrationsystem.model.dto.StudentDto;
import com.school.registrationsystem.repository.CourseRepository;
import com.school.registrationsystem.repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@RequiredArgsConstructor
class StudentServiceTest {
    //    private final StudentTestData studentTestData;
//    private final CourseTestData courseTestData;
    private StudentService studentService;
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private StudentRepository studentRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        studentService = new StudentService(studentRepository, courseRepository);
    }

    @Test
    void initalizeStudent_nullValues() {
        assertThrows(NullPointerException.class, () -> StudentDto.builder().name(null).surname("surname").build());
        assertThrows(NullPointerException.class, () -> StudentDto.builder().name("name").surname(null).build());
    }

    @Test
    void modifyStudent() {
        int studentId = 1;
        StudentDto modifiedStudentDto = new StudentDto();
        modifiedStudentDto.setName("Modified name");
        modifiedStudentDto.setSurname("Modified surname");

        Student existingStudent = new Student();
        existingStudent.setStudentId(studentId);

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(existingStudent));

        studentService.modifyStudent(modifiedStudentDto, studentId);

        assertEquals("Modified name", existingStudent.getName());
        assertEquals("Modified surname", existingStudent.getSurname());
    }

    @Test
    void modifyStudent_nullStudent() {
        int studentId = 1;
        StudentDto modifiedStudentDto = new StudentDto();
        modifiedStudentDto.setName("Modified name");
        modifiedStudentDto.setSurname("Modified surname");

        Student existingStudent = new Student();
        existingStudent.setStudentId(studentId);

        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());
        Assertions.assertThrows(EntityNotFoundException.class, () -> studentService.modifyStudent(modifiedStudentDto, studentId));
    }

    @Test
    void getAllStudents() {
        Integer courseId = null;
        Boolean withoutCourses = null;
        Pageable pageable = Pageable.unpaged();
        Page<Student> expectedPage = new PageImpl<>(new ArrayList<>());

        when(studentRepository.findAll(Mockito.any(Specification.class), eq(pageable))).thenReturn(expectedPage);

        Page<Student> resultPage = studentService.getAll(courseId, withoutCourses, pageable);

        assertEquals(expectedPage, resultPage);
    }

    @Test
    void getAllStudentsByCourseId() {
        Integer courseId = 1;
        Boolean withoutCourses = null;
        Pageable pageable = Pageable.unpaged();
        Page<Student> expectedPage = new PageImpl<>(new ArrayList<>());

        when(studentRepository.findAll(Mockito.any(Specification.class), eq(pageable))).thenReturn(expectedPage);

        Page<Student> resultPage = studentService.getAll(courseId, withoutCourses, pageable);

        assertEquals(expectedPage, resultPage);
    }

    @Test
    void getAllStudentsWithoutCourses() {
        Integer courseId = null;
        Boolean withoutCourses = true;
        Pageable pageable = Pageable.unpaged();
        Page<Student> expectedPage = new PageImpl<>(new ArrayList<>());

        when(studentRepository.findAll(Mockito.any(Specification.class), eq(pageable))).thenReturn(expectedPage);

        Page<Student> resultPage = studentService.getAll(courseId, withoutCourses, pageable);

        assertEquals(expectedPage, resultPage);
    }
}