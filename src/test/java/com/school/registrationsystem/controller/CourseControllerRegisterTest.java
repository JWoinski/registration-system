package com.school.registrationsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.school.registrationsystem.model.Course;
import com.school.registrationsystem.model.Student;
import com.school.registrationsystem.model.dto.RegisterDto;
import com.school.registrationsystem.repository.CourseRepository;
import com.school.registrationsystem.repository.StudentRepository;
import com.school.registrationsystem.service.CourseService;
import com.school.registrationsystem.service.StudentService;
import com.school.registrationsystem.testData.CourseControllerTestData;
import com.school.registrationsystem.testData.StudentControllerTestData;
import jakarta.persistence.EntityNotFoundException;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CourseControllerRegisterTest {
    private static ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private CourseService courseService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private StudentControllerTestData studentTest;
    @Autowired
    private CourseControllerTestData courseTest;

    @BeforeAll
    static void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void registerStudentToCourse() throws Exception {
        Student savedStudent = studentRepository.save(studentTest.testStudent_correctValues());

        Course savedCourse = courseRepository.save(courseTest.testCourse_correctValues());

        RegisterDto registerDto = RegisterDto.builder().studentId(savedStudent
                        .getStudentId())
                .courseId(savedCourse.getCourseId()).build();

        String registerDtoJson = objectMapper.writeValueAsString(registerDto);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = post("/courses/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registerDtoJson);

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().isOk());

        Optional<Student> updatedStudent = Optional.ofNullable(studentRepository.findById(savedStudent.getStudentId()).orElseThrow(() -> new EntityNotFoundException("Student not found.")));
        Optional<Course> updatedCourse = Optional.ofNullable(courseRepository.findById(savedCourse.getCourseId()).orElseThrow(() -> new EntityNotFoundException("Course not found.")));

        int updatedStudentId = updatedCourse.get().getStudentList().stream().findFirst().get().getStudentId();
        int updatedCourseId = updatedStudent.get().getCourseList().stream().findFirst().get().getCourseId();

        assertEquals(updatedStudentId, savedStudent.getStudentId());
        assertEquals(updatedCourseId, savedCourse.getCourseId());

        courseService.deleteCourse(savedCourse.getCourseId());
        studentService.deleteStudent(savedStudent.getStudentId());
    }

    @Test
    void registerStudentToCourse_notFoundStudent() throws Exception {
        Student savedStudent = studentTest.testStudent_NullValues();

        Course savedCourse = courseRepository.save(courseTest.testCourse_correctValues());

        RegisterDto registerDto = RegisterDto.builder()
                .studentId(savedStudent.getStudentId())
                .courseId(savedCourse.getCourseId()).build();

        String registerDtoJson = objectMapper.writeValueAsString(registerDto);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = post("/courses/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registerDtoJson);

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().isNotFound());

        courseService.deleteCourse(savedCourse.getCourseId());
    }

    @Test
    void registerStudentToCourse_notFoundCourse() throws Exception {
        Student savedStudent = studentRepository.save(studentTest.testStudent_correctValues());

        Course savedCourse = courseTest.testCourse_NullValues();

        RegisterDto registerDto = RegisterDto.builder()
                .studentId(savedStudent.getStudentId())
                .courseId(savedCourse.getCourseId()).build();

        String registerDtoJson = objectMapper.writeValueAsString(registerDto);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = post("/courses/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registerDtoJson);

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().isNotFound());

        studentService.deleteStudent(savedStudent.getStudentId());
    }

    @Test
    void registerStudentToCourse_EndDateIsOutOfTime() throws Exception {
        //TODO register course end time before localdate.now
        Student savedStudent = studentRepository.save(studentTest.testStudent_correctValues());

        Course savedCourse = courseRepository.save(courseTest.testCourse_pastEndDate());

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = post("/courses/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(courseTest.genereateRegisterDto(savedStudent.getStudentId(), savedCourse.getCourseId()));

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().isBadRequest());

        courseService.deleteCourse(savedCourse.getCourseId());
        studentService.deleteStudent(savedStudent.getStudentId());
    }

    @Test
    void registerStudentToCourse_StudentGot5CoursesAlready() throws Exception {
        //TODO register student got 5 courses already
        Student savedStudent = studentRepository.save(studentTest.testStudent_correctValues());

        List<Course> courseList = courseRepository.saveAll(courseTest.testList5Courses());
        courseTest.registerCourseListToStudent(savedStudent.getStudentId(), courseList);

        Course savedCourse = courseRepository.save(courseTest.testCourse_correctValues());

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = post("/courses/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(courseTest.genereateRegisterDto(savedStudent.getStudentId(), savedCourse.getCourseId()));

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().isBadRequest());

        courseService.deleteCourse(savedCourse.getCourseId());
        studentService.deleteStudent(savedStudent.getStudentId());
        courseRepository.deleteAll();
    }

    @Test
    void registerStudentToCourse_CourseGot50StudentsAlready() throws Exception {
        //TODO register course got 50 students already
        Course savedCourse = courseRepository.save(courseTest.testCourse_correctValues());

        List<Student> studentList = studentRepository.saveAll(studentTest.testList50Students());
        courseTest.registerStudentListToCourse(savedCourse.getCourseId(), studentList);

        Student savedStudent = studentRepository.save(studentTest.testStudent_correctValues());

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = post("/courses/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(courseTest.genereateRegisterDto(savedStudent.getStudentId(), savedCourse.getCourseId()));

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().isBadRequest());

        courseService.deleteCourse(savedCourse.getCourseId());
        studentService.deleteStudent(savedStudent.getStudentId());
        studentRepository.deleteAll(studentList);
    }

    @Test
    void registerStudentToCourse_StudentAlreadyEnrolledCourse() throws Exception {
        //TODO register student already enrolled course
        Student savedStudent = studentRepository.save(studentTest.testStudent_correctValues());

        Course savedCourse = courseRepository.save(courseTest.testCourse_pastEndDate());

        courseService.registerStudentToCourse(new RegisterDto(savedStudent.getStudentId(), savedCourse.getCourseId()));

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = post("/courses/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(courseTest.genereateRegisterDto(savedStudent.getStudentId(), savedCourse.getCourseId()));

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().isBadRequest());

        courseService.deleteCourse(savedCourse.getCourseId());
        studentService.deleteStudent(savedStudent.getStudentId());
    }
}
