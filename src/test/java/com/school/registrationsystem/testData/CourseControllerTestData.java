package com.school.registrationsystem.testData;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.school.registrationsystem.model.Course;
import com.school.registrationsystem.model.Student;
import com.school.registrationsystem.model.dto.CourseDto;
import com.school.registrationsystem.model.dto.RegisterDto;
import com.school.registrationsystem.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class CourseControllerTestData {
    private final ObjectMapper objectMapper;
    private final CourseService courseService;
    private StudentControllerTestData studentTest;

    public CourseDto testCourseDto_correctValues() {
        return CourseDto.builder()
                .name("Nazwa kursu")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(7))
                .description("Opis kursu")
                .build();
    }

    public Course testCourse_correctValues() {
        return Course.builder()
                .name("Nazwa kursu")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(7))
                .description("Opis kursu")
                .build();
    }

    public Course testCourse_NullValues() {
        return new Course();
    }

    public CourseDto testCourse_ModificatedValues() {
        return CourseDto.builder()
                .name("Nowa nazwa kursu")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(14))
                .description("Nowy opis kursu")
                .build();
    }

    public Course testCourse_CorrectValues1() {
        return Course.builder()
                .name("Nazwa kursu 1")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(7))
                .description("Opis kursu 1")
                .build();
    }

    public Course testCourse_CorrectValues2() {
        return Course.builder()
                .name("Nazwa kursu 2")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(7))
                .description("Opis kursu 2")
                .build();

    }

    public List<Course> testList5Courses() {
        return IntStream.range(0, 5)
                .mapToObj(i -> Course.builder()
                        .courseId(i + 1)
                        .name("Nazwa kursu " + (i + 1))
                        .startDate(LocalDate.now())
                        .endDate(LocalDate.now().plusDays(7))
                        .description("Opis kursu " + (i + 1))
                        .build())
                .toList();
    }

    public Course testCourse_pastEndDate() {
        return Course.builder()
                .startDate(LocalDate.now().minusDays(10))
                .endDate(LocalDate.now().minusDays(1))
                .build();
    }

    public String genereateRegisterDto(int studentId, int courseId) throws JsonProcessingException {
        RegisterDto registerDto = RegisterDto.builder()
                .studentId(studentId)
                .courseId(courseId)
                .build();
        return objectMapper.writeValueAsString(registerDto);

    }

    public void registerCourseListToStudent(int studentId, List<Course> courseList) {
        for (Course course : courseList) {
            courseService.registerStudentToCourse(new RegisterDto(studentId, course.getCourseId()));
        }
    }

    public void registerStudentListToCourse(int courseId, List<Student> studentList) {
        for (Student student : studentList) {
            courseService.registerStudentToCourse(new RegisterDto(student.getStudentId(), courseId));
        }
    }
}
