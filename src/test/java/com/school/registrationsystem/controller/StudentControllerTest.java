package com.school.registrationsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.school.registrationsystem.model.Course;
import com.school.registrationsystem.model.Student;
import com.school.registrationsystem.model.dto.RegisterDto;
import com.school.registrationsystem.model.dto.StudentDto;
import com.school.registrationsystem.repository.CourseRepository;
import com.school.registrationsystem.repository.StudentRepository;
import com.school.registrationsystem.service.CourseService;
import com.school.registrationsystem.testData.CourseControllerTestData;
import com.school.registrationsystem.testData.StudentControllerTestData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTest {
    private static ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private CourseService courseService;
    @Autowired
    private StudentControllerTestData studentTest;
    @Autowired
    private CourseControllerTestData courseTest;

    @BeforeAll
    static void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @AfterEach
    void clean() {
        courseRepository.deleteAll();
        studentRepository.deleteAll();
    }

    @Test
    void saveStudent() throws Exception {
        StudentDto studentDto = studentTest.testStudentDto_correctValues();

        String studentDtoJson = objectMapper.writeValueAsString(studentDto);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = post("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(studentDtoJson);

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().isCreated());
    }

    @Test
    void deleteStudent() throws Exception {
        Student saveStudent = studentRepository.save(studentTest.testStudent_correctValues());

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = delete("/students")
                .param("studentId", String.valueOf(saveStudent.getStudentId()));

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().isOk());

        assertFalse(courseRepository.existsById(saveStudent.getStudentId()));
    }

    @Test
    void deleteStudent_nullStudent() throws Exception {
        Student nonSaveStudent = studentTest.testStudent_NullValues();

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = delete("/students")
                .param("studentId", String.valueOf(nonSaveStudent.getStudentId()));

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().isNotFound());

        assertFalse(courseRepository.existsById(nonSaveStudent.getStudentId()));
    }

    @Test
    void modifyStudent() throws Exception {
        Student savedStudent = studentRepository.save(studentTest.testStudent_correctValues());

        StudentDto modifiedStudentDto = studentTest.testStudentDto_modifiedValues();

        String modifiedStudentDtoJson = objectMapper.writeValueAsString(modifiedStudentDto);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = put("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(modifiedStudentDtoJson)
                .param("studentId", String.valueOf(savedStudent.getStudentId()));

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().isOk());

        Optional<Student> modifiedStudent = studentRepository.findById(savedStudent.getStudentId());
        assertTrue(modifiedStudent.isPresent());
        assertEquals("nowe imie studenta", modifiedStudent.get().getName());
        assertEquals("nowe nazwisko studenta", modifiedStudent.get().getSurname());
    }

    @Test
    void getAll() throws Exception {
        Student savedStudent1 = studentRepository.save(studentTest.testStudent_correctValues1());
        Student savedStudent2 = studentRepository.save(studentTest.testStudent_correctValues2());

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = get("/students");
        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.content[0].name").value(savedStudent1.getName()))
                .andExpect(jsonPath("$.content[1].name").value(savedStudent2.getName()));
    }

    @Test
    void getAll_withoutCourses() throws Exception {
        Course savedCourse1 = courseRepository.save(courseTest.testCourse_CorrectValues1());

        Student savedStudent1 = studentRepository.save(studentTest.testStudent_correctValues1());
        Student savedStudent2 = studentRepository.save(studentTest.testStudent_correctValues2());

        courseService.registerStudentToCourse(RegisterDto.builder()
                .courseId(savedCourse1.getCourseId())
                .studentId(savedStudent1.getStudentId())
                .build());


        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = get("/students")
                .param("withoutCourses", String.valueOf(true));

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content[0].name").value(savedStudent2.getName()));

        courseService.deleteCourse(savedCourse1.getCourseId());
    }

    @Test
    void getAll_byCourseId() throws Exception {
        Course savedCourse1 = courseRepository.save(courseTest.testCourse_CorrectValues1());

        Student savedStudent = studentRepository.save(studentTest.testStudent_correctValues());

        courseService.registerStudentToCourse(RegisterDto.builder()
                .courseId(savedCourse1.getCourseId())
                .studentId(savedStudent.getStudentId())
                .build());

        Course savedCourse2 = courseRepository.save(courseTest.testCourse_CorrectValues2());

        courseService.registerStudentToCourse(RegisterDto.builder()
                .courseId(savedCourse2.getCourseId())
                .studentId(savedStudent.getStudentId())
                .build());

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = get("/students")
                .param("courseId", String.valueOf(savedCourse2.getCourseId()));

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content[0].name").value(savedStudent.getName()));

        courseService.deleteCourse(savedCourse1.getCourseId());
        courseService.deleteCourse(savedCourse2.getCourseId());
    }
}
