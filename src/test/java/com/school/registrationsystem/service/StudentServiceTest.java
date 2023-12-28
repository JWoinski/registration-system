package com.school.registrationsystem.service;

import com.school.registrationsystem.exception.CapacityException;
import com.school.registrationsystem.exception.DuplicateEnrollmentException;
import com.school.registrationsystem.exception.TimeOutCourseRegisterException;
import com.school.registrationsystem.model.Course;
import com.school.registrationsystem.model.Student;
import com.school.registrationsystem.model.dto.RegisterDto;
import com.school.registrationsystem.model.dto.StudentDto;
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
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
class StudentServiceTest {
    @Autowired
    private StudentService studentService;
    @Autowired
    private StudentTestData studentTestData;
    @Autowired
    private CourseService courseService;

    @Test
    void modifyStudent() {
        Student student = StudentTestData.testStudent();
        studentService.saveStudent(student);

        Student expectedStudent = Student.builder().name("newName").studentIndex(student.getStudentIndex()).surname("newSurname").build();

        studentService.modifyStudent(studentTestData.testExpectedStudentDto(student.getStudentIndex()));
        Student actualStudent = studentService.getStudentByStudentIndex(student.getStudentIndex());

        assertEquals(expectedStudent.getSurname(), actualStudent.getSurname());

        assertEquals(expectedStudent.getName(), actualStudent.getName());

        assertEquals(expectedStudent.getStudentIndex(), actualStudent.getStudentIndex());
    }

    @Test
    void modifyNullStudentIndex() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> studentService.modifyStudent(new StudentDto("name", "surname", 1)));
    }

    @Test
    void modifyNullStudentDto() {
        Assertions.assertThrows(NullPointerException.class, () -> studentService.modifyStudent(new StudentDto()));
    }

    @Test
    void getAll() {
        List<Student> studentList = StudentTestData.testStudentList();
        studentService.saveAll(studentList);
        int expected = 2;
        int actual = studentService.getAll().size();
        assertEquals(expected, actual);
    }

    @Test
    void registerStudentToCourse() {
        Student student = StudentTestData.testStudent();
        studentService.saveStudent(student);

        Course course = CourseTestData.testCourse();
        courseService.saveCourse(course);

        studentService.registerStudentToCourse(new RegisterDto(student.getStudentIndex(), course.getCourseIndex()));

        Student updatedStudent = studentService.getStudentByStudentIndex(student.getStudentIndex());
        assertNotNull(updatedStudent.getCourseList(), "Student list in the course should not be null");

        assertEquals(1, updatedStudent.getCourseList().size(), "Incorrect number of students in the course");
    }

    @Test
    void registerStudentToCourse_shouldThrowDuplcateException() {
        Student student = StudentTestData.testStudent();
        studentService.saveStudent(student);

        Course course = CourseTestData.testCourse();
        courseService.saveCourse(course);

        studentService.registerStudentToCourse(new RegisterDto(student.getStudentIndex(), course.getCourseIndex()));

        Assertions.assertThrows(DuplicateEnrollmentException.class, () -> studentService.registerStudentToCourse(new RegisterDto(student.getStudentIndex(), course.getCourseIndex())));
    }

    @Test
    void registerStudentToCourse_shouldThrowEntityNotFoundException() {
        Student student = StudentTestData.testStudent();
        studentService.saveStudent(student);

        Course course = CourseTestData.testCourse();
        courseService.saveCourse(course);

        Assertions.assertThrows(EntityNotFoundException.class, () -> studentService.registerStudentToCourse(new RegisterDto(student.getStudentIndex(), 1)));
    }

    @Test
    void registerStudentToCourse_shouldTimeOutCourseRegisterException() {
        Student student = StudentTestData.testStudent();
        studentService.saveStudent(student);

        Course course = Course.builder().name("name").startDate(LocalDate.now().minusMonths(2)).endDate(LocalDate.now().minusMonths(1)).description("description").build();
        courseService.saveCourse(course);

        Assertions.assertThrows(TimeOutCourseRegisterException.class, () -> studentService.registerStudentToCourse(new RegisterDto(student.getStudentIndex(), course.getCourseIndex())));
    }


    @Test
    void registerStudentToCourse_shouldNotRegisterCauseMax50StudentsPer1Course() {
        Course course = Course.builder()
                .name("name")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusMonths(1))
                .courseIndex(1)
                .description("description")
                .build();
        courseService.saveCourse(course);

        List<Student> students = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            Student student = StudentTestData.testStudent();
            studentService.saveStudent(student);
            students.add(student);
        }

        for (Student student : students) {
            studentService.registerStudentToCourse(new RegisterDto(student.getStudentIndex(), course.getCourseIndex()));
        }

        Student student51 = StudentTestData.testStudent();
        studentService.saveStudent(student51);
        Assertions.assertThrows(CapacityException.class, () -> studentService.registerStudentToCourse(new RegisterDto(student51.getStudentIndex(), course.getCourseIndex())));
    }

    @Test
    void registerStudentToCourse_shouldNotRegisterCauseMax5CoursePer1Student() {
        Student student = StudentTestData.testStudent();
        studentService.saveStudent(student);

        List<Course> courses = new ArrayList<>();

        for (int i = 1; i <= 6; i++) {
            Course course = Course.builder()
                    .name("name")
                    .startDate(LocalDate.now())
                    .endDate(LocalDate.now().plusMonths(1))
                    .courseIndex(i)
                    .description("description")
                    .build();
            courses.add(course);
            courseService.saveCourse(course);
        }

        for (int i = 0; i < 5; i++) {
            studentService.registerStudentToCourse(new RegisterDto(student.getStudentIndex(), courses.get(i).getCourseIndex()));
        }

        Assertions.assertThrows(CapacityException.class, () -> studentService.registerStudentToCourse(new RegisterDto(student.getStudentIndex(), courses.get(5).getCourseIndex())));
    }

    @Test
    void getStudentListByCourseIndexTest() {
        Student student = StudentTestData.testStudent();
        studentService.saveStudent(student);

        Course course = CourseTestData.testCourse();
        courseService.saveCourse(course);


        studentService.registerStudentToCourse(new RegisterDto(student.getStudentIndex(), course.getCourseIndex()));
        Integer actual = studentService.getStudentListByCourseIndex(course.getCourseIndex()).stream().map(Student::getStudentIndex).findFirst().get();
        assertEquals(student.getStudentIndex(), actual);
    }

    @Test
    void getStudentListByCourseIndexTest_shouldThrowException() {
        Student student = StudentTestData.testStudent();
        studentService.saveStudent(student);

        Course course = CourseTestData.testCourse();
        courseService.saveCourse(course);

        Assertions.assertThrows(EntityNotFoundException.class, () -> studentService.registerStudentToCourse(new RegisterDto(student.getStudentIndex(), 1)));
    }

    @Test
    void getStudentByEmptyCourseList() {
        Student student1 = Student.builder().name("name").studentIndex(1234).surname("surname").build();
        Student student2 = Student.builder().name("name").studentIndex(1235).surname("surname").build();
        studentService.saveStudent(student1);
        studentService.saveStudent(student2);

        Course course = CourseTestData.testCourse();
        courseService.saveCourse(course);

        studentService.registerStudentToCourse(new RegisterDto(student1.getStudentIndex(), course.getCourseIndex()));

        assertEquals(1, studentService.getStudentByEmptyCourseList().size());
    }
}