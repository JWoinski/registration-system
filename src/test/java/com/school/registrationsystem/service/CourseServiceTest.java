package com.school.registrationsystem.service;

import com.school.registrationsystem.model.Course;
import com.school.registrationsystem.model.Student;
import com.school.registrationsystem.model.dto.CourseDto;
import com.school.registrationsystem.model.dto.RegisterDto;
import com.school.registrationsystem.service.TestData.CourseTestData;
import com.school.registrationsystem.service.TestData.StudentTestData;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
class CourseServiceTest {
    @Autowired
    private CourseService courseService;
    @Autowired
    private StudentService studentService;

    @Test
    void modifyCourse() {
        Course course = CourseTestData.testCourse();
        courseService.saveCourse(course);

        Course testCourse = courseService.getCourseByCourseIndex(course.getCourseIndex());
        Course expectedCourse = CourseTestData.testExpectedCourse(testCourse);

        courseService.modifyCourse(CourseTestData.testExpectedCourseDto(course.getCourseIndex()));

        Course acutalCourse = courseService.getCourseByCourseIndex(course.getCourseIndex());

        Assertions.assertEquals(expectedCourse.getDescription(), acutalCourse.getDescription());

        Assertions.assertEquals(expectedCourse.getEndDate(), acutalCourse.getEndDate());

        Assertions.assertEquals(expectedCourse.getStartDate(), acutalCourse.getStartDate());

        Assertions.assertEquals(expectedCourse.getCourseIndex(), acutalCourse.getCourseIndex());

    }

    @Test
    void modifyNullStudentIndex() {
        // DB is empty so we can not found anything in empty DB
        Assertions.assertThrows(EntityNotFoundException.class, () -> courseService.modifyCourse(CourseDto.builder()
                .name("name").startDate(LocalDate.now())
                .endDate(LocalDate.now()
                        .plusMonths(1))
                .courseIndex(1)
                .description("descrption")
                .build()));
    }

    @Test
    void modifyNullStudentDto() {
        Assertions.assertThrows(NullPointerException.class, () -> courseService.modifyCourse(null));
    }

    @Test
    void getAll() {
        List<Course> courses = CourseTestData.testCourseList();
        courseService.saveAll(courses);
        int expected = 3;
        int actual = courseService.getAll().size();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getCourseListByStudentIndex() {
        Course course1 = Course.builder().name("name").courseIndex(1234).description("description").startDate(LocalDate.now()).endDate(LocalDate.now().plusMonths(1)).build();
        courseService.saveCourse(course1);
        Course course2 = Course.builder().name("name").courseIndex(1235).description("description").startDate(LocalDate.now()).endDate(LocalDate.now().plusMonths(1)).build();
        courseService.saveCourse(course2);
        Student student = Student.builder().studentIndex(1234).build();
        studentService.saveStudent(student);
        studentService.registerStudentToCourse(new RegisterDto(student.getStudentIndex(), course1.getCourseIndex()));
        studentService.registerStudentToCourse(new RegisterDto(student.getStudentIndex(), course2.getCourseIndex()));

        Assertions.assertEquals(2, courseService.getCourseListByStudentIndex(student.getStudentIndex()).size());
    }

    @Test
    void getCourseListByStudentIndex_shouldThrowEntityNotFound() {
        Course course1 = Course.builder().name("name").courseIndex(1234).description("description").startDate(LocalDate.now()).endDate(LocalDate.now().plusMonths(1)).build();
        courseService.saveCourse(course1);
        Course course2 = Course.builder().name("name").courseIndex(1235).description("description").startDate(LocalDate.now()).endDate(LocalDate.now().plusMonths(1)).build();
        courseService.saveCourse(course2);
        Student student = Student.builder().studentIndex(1234).build();
        studentService.saveStudent(student);
        studentService.registerStudentToCourse(new RegisterDto(student.getStudentIndex(), course1.getCourseIndex()));
        studentService.registerStudentToCourse(new RegisterDto(student.getStudentIndex(), course2.getCourseIndex()));

        Assertions.assertThrows(EntityNotFoundException.class, () -> studentService.registerStudentToCourse(new RegisterDto(1, 2)));
    }


    @Test
    void getCourseListByEmptyStudentList() {
        Student student = StudentTestData.testStudent();
        studentService.saveStudent(student);

        List<Course> courses = CourseTestData.testCourseListWith2EmptyCourses();
        courseService.saveAll(courses);

        studentService.registerStudentToCourse(new RegisterDto(student.getStudentIndex(), courses.stream().findFirst().get().getCourseIndex()));

        int expected = 2;
        int actual = courseService.getCourseListByEmptyStudentList().size();

        Assertions.assertEquals(expected, actual);

    }
}