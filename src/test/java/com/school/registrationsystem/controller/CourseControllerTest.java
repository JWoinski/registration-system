package com.school.registrationsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.school.registrationsystem.model.Course;
import com.school.registrationsystem.model.Student;
import com.school.registrationsystem.model.dto.CourseDto;
import com.school.registrationsystem.model.dto.RegisterDto;
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

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CourseControllerTest {
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
    private CourseControllerTestData courseTest;
    @Autowired
    private StudentControllerTestData studentTest;

    @BeforeAll
    static void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @AfterEach
    void clean() {
        courseRepository.deleteAll();
    }

    @Test
    void saveCourse() throws Exception {
        CourseDto courseDto = courseTest.testCourseDto_correctValues();

        String courseDtoJson = objectMapper.writeValueAsString(courseDto);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = post("/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(courseDtoJson);

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().isCreated());
    }

    @Test
    void deleteCourse() throws Exception {
        Course savedCourse = courseRepository.save(courseTest.testCourse_correctValues());

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = delete("/courses")
                .param("courseId", String.valueOf(savedCourse.getCourseId()));

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().isOk());

        assertFalse(courseRepository.existsById(savedCourse.getCourseId()));
    }

    @Test
    void deleteCourse_NullCourse() throws Exception {
        Course nonSavedCourse = courseTest.testCourse_NullValues();

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = delete("/courses")
                .param("courseId", String.valueOf(nonSavedCourse.getCourseId()));

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().isNotFound());

        assertFalse(courseRepository.existsById(nonSavedCourse.getCourseId()));
    }

    @Test
    void modifyCourse() throws Exception {
        Course savedCourse = courseRepository.save(courseTest.testCourse_correctValues());

        CourseDto modifiedCourseDto = courseTest.testCourse_ModificatedValues();

        String modifiedCourseDtoJson = objectMapper.writeValueAsString(modifiedCourseDto);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = put("/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(modifiedCourseDtoJson)
                .param("courseId", String.valueOf(savedCourse.getCourseId()));

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().isOk());

        Optional<Course> modifiedCourse = courseRepository.findById(savedCourse.getCourseId());
        assertTrue(modifiedCourse.isPresent());
        assertEquals("Nowa nazwa kursu", modifiedCourse.get().getName());
        assertEquals(LocalDate.now().plusDays(14), modifiedCourse.get().getEndDate());
        assertEquals("Nowy opis kursu", modifiedCourse.get().getDescription());
    }

    @Test
    void getAll() throws Exception {
        Course savedCourse1 = courseRepository.save(courseTest.testCourse_CorrectValues1());
        Course savedCourse2 = courseRepository.save(courseTest.testCourse_CorrectValues2());

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = get("/courses");
        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.content[0].name").value(savedCourse1.getName()))
                .andExpect(jsonPath("$.content[1].name").value(savedCourse2.getName()));
    }

    @Test
    void getAll_withoutStudents() throws Exception {
        Course savedCourse1 = courseRepository.save(courseTest.testCourse_CorrectValues1());

        Student savedStudent = studentRepository.save(studentTest.testStudent_correctValues());

        courseService.registerStudentToCourse(RegisterDto.builder()
                .courseId(savedCourse1.getCourseId())
                .studentId(savedStudent.getStudentId())
                .build());

        Course savedCourse2 = courseRepository.save(courseTest.testCourse_CorrectValues2());

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = get("/courses")
                .param("withoutStudents", String.valueOf(true));

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content[0].name").value(savedCourse2.getName()));

        courseService.deleteCourse(savedCourse1.getCourseId());
    }

    @Test
    void getAll_byStudentId() throws Exception {
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

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = get("/courses")
                .param("studentId", String.valueOf(savedStudent.getStudentId()));

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.content[0].name").value(savedCourse1.getName()))
                .andExpect(jsonPath("$.content[1].name").value(savedCourse2.getName()));

        courseService.deleteCourse(savedCourse1.getCourseId());
        courseService.deleteCourse(savedCourse2.getCourseId());
    }
}