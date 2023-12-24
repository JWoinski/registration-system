package com.school.registrationsystem.service.TestData;

import com.school.registrationsystem.model.Course;
import com.school.registrationsystem.model.dto.CourseDto;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CourseTestData {

    public static Course testCourse() {

        return Course.builder()
                .name("name")
                .courseIndex(1234)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusMonths(3))
                .description("description")
                .build();
    }

    public static Course testExpectedCourse(Course testCourse) {
        testCourse.setStartDate(LocalDate.now().minusMonths(1));
        testCourse.setDescription("new");
        testCourse.setEndDate(LocalDate.now().plusMonths(5));

        return testCourse;
    }

    public static CourseDto testExpectedCourseDto() {
        return CourseDto.builder()
                .name("name")
                .courseIndex(1234)
                .startDate(LocalDate.now().minusMonths(1))
                .endDate(LocalDate.now().plusMonths(5))
                .description("new")
                .build();
    }

    public static List<Course> testCourseList() {
        return List.of(
                Course.builder()
                        .name("name")
                        .courseIndex(1234)
                        .startDate(LocalDate.now())
                        .endDate(LocalDate.now().plusMonths(3))
                        .description("description")
                        .build(),
                Course.builder()
                        .name("name")
                        .courseIndex(1235)
                        .startDate(LocalDate.now())
                        .endDate(LocalDate.now().plusMonths(3))
                        .description("description")
                        .build(),
                Course.builder()
                        .name("name")
                        .courseIndex(1236)
                        .startDate(LocalDate.now())
                        .endDate(LocalDate.now().plusMonths(3))
                        .description("description")
                        .build()
        );
    }

    public static List<Course> testCourseListWith2EmptyCourses() {
        return List.of(
                Course.builder()
                        .name("name")
                        .courseIndex(1234)
                        .startDate(LocalDate.now())
                        .endDate(LocalDate.now().plusMonths(3))
                        .description("description")
                        .build(),
                Course.builder()
                        .name("name")
                        .courseIndex(1235)
                        .startDate(LocalDate.now())
                        .endDate(LocalDate.now().plusMonths(3))
                        .description("description")
                        .build(),
                Course.builder()
                        .name("name")
                        .courseIndex(1236)
                        .startDate(LocalDate.now())
                        .endDate(LocalDate.now().plusMonths(3))
                        .description("description")
                        .build()
        );
    }
}