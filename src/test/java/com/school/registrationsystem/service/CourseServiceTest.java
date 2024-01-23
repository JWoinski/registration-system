package com.school.registrationsystem.service;

import com.school.registrationsystem.exception.CapacityException;
import com.school.registrationsystem.exception.CourseClosedException;
import com.school.registrationsystem.exception.DateValidException;
import com.school.registrationsystem.exception.StudentAlreadyEnrolledException;
import com.school.registrationsystem.model.Course;
import com.school.registrationsystem.model.Student;
import com.school.registrationsystem.model.dto.CourseDto;
import com.school.registrationsystem.model.dto.RegisterDto;
import com.school.registrationsystem.repository.CourseRepository;
import com.school.registrationsystem.repository.StudentRepository;
import com.school.registrationsystem.testData.CourseControllerTestData;
import com.school.registrationsystem.testData.StudentControllerTestData;
import com.school.registrationsystem.validator.registrationPeriod.CourseOpenValidator;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
class CourseServiceTest {
    private CourseService courseService;
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private StudentRepository studentRepository;
    @InjectMocks
    private StudentControllerTestData studentTest;
    @InjectMocks
    private CourseControllerTestData courseTest;
    @InjectMocks
    private CourseOpenValidator courseOpenValidator;
    @Value("${course.students.threshold}")
    private int courseCapacity;
    @Value("${student.courses.threshold}")
    private int studentCapacity;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        courseService = new CourseService(courseRepository, studentRepository);
    }

    @Test
    void InitalizationCourse_nulllValues() {
        //null description
        assertThrows(NullPointerException.class, () -> new CourseDto("name", LocalDate.now(), LocalDate.now().plusDays(1), null));
        //null endDate
        assertThrows(NullPointerException.class, () -> new CourseDto("name", LocalDate.now(), null, "description"));
        //null startDate
        assertThrows(NullPointerException.class, () -> new CourseDto("name", null, LocalDate.now(), "description"));
        //null name
        assertThrows(NullPointerException.class, () -> new CourseDto(null, LocalDate.now(), LocalDate.now(), "description"));
    }

    @Test
    void initalizeCourse_shouldThrowExcepetion_causeStartDateIsAfterEndDate() {
        assertThrows(DateValidException.class, () -> courseService.saveCourse(new CourseDto("name", LocalDate.now(), LocalDate.now().minusYears(1), "description")));
    }

    @Test
    void modifyCourse() {
        int courseId = 1;
        CourseDto modifiedCourseDto = CourseDto.builder()
                .name("Modified Course")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(14))
                .description("Modified Description")
                .build();

        Course existingCourse = Course.builder()
                .courseId(courseId)
                .name("Original Course")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(7))
                .description("Original Description")
                .build();

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(existingCourse));

        courseService.modifyCourse(modifiedCourseDto, courseId);

        verify(courseRepository, times(1)).save(Mockito.any());
        assertEquals("Modified Course", existingCourse.getName());
        assertEquals(LocalDate.now().plusDays(14), existingCourse.getEndDate());
        assertEquals("Modified Description", existingCourse.getDescription());
    }

    @Test
    void modifyCourse_NullCourse() {
        int courseId = 1;
        CourseDto modifiedCourseDto = CourseDto.builder()
                .name("Modified Course")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(14))
                .description("Modified Description")
                .build();

        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> courseService.modifyCourse(modifiedCourseDto, courseId));
    }

    @Test
    void registerStudentToCourse_Success() {
        RegisterDto registerDto = new RegisterDto();
        registerDto.setStudentId(1);
        registerDto.setCourseId(2);

        Student student = studentTest.testStudent_correctValues();
        student.setStudentId(1);
        student.setCourseList(new ArrayList<>());
        Course course = courseTest.testCourse_correctValues();
        course.setStudentList(new ArrayList<>());
        course.setCourseId(2);

        when(studentRepository.findById(1)).thenReturn(Optional.of(student));
        when(courseRepository.findById(2)).thenReturn(Optional.of(course));

        courseService.registerStudentToCourse(registerDto);

        assertTrue(student.getCourseList().contains(course));
        assertTrue(course.getStudentList().contains(student));
    }

    @Test
    void registerStudentToCourse_StudentNotFound() {
        RegisterDto registerDto = new RegisterDto();
        registerDto.setStudentId(1);
        registerDto.setCourseId(2);

        when(studentRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> courseService.registerStudentToCourse(registerDto));
    }

    @Test
    void registerStudentToCourse_CourseNotFound() {
        RegisterDto registerDto = new RegisterDto();
        registerDto.setStudentId(1);
        registerDto.setCourseId(2);

        Student student = new Student();
        student.setStudentId(1);

        when(studentRepository.findById(1)).thenReturn(Optional.of(student));
        when(courseRepository.findById(2)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> courseService.registerStudentToCourse(registerDto));
    }
    @Test
    void isValid_CourseNotFound_ThrowsEntityNotFoundException() {
        RegisterDto registerDto = RegisterDto.builder()
                .studentId(1)
                .courseId(999)
                .build();

        when(courseRepository.findById(registerDto.getCourseId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> courseOpenValidator.isValid(registerDto, null));
    }

    @Test
    void registerStudentToCourse_EndDateIsOutOfTime() {
        Course course = Course.builder()
                .courseId(1)
                .endDate(LocalDate.now().minusDays(1))
                .build();
        Student student = Student.builder()
                .studentId(1)
                .surname("surname")
                .name("name")
                .build();
        RegisterDto registerDto = RegisterDto.builder()
                .studentId(course.getCourseId())
                .courseId(student.getStudentId())
                .build();
        when(courseRepository.findById(registerDto.getCourseId())).thenReturn(Optional.of(course));
        when(studentRepository.findById(registerDto.getStudentId())).thenReturn(Optional.of(student));

        Assertions.assertThrows(CourseClosedException.class, () -> courseService.registerStudentToCourse(registerDto));
    }
    @Test
    void isValid_CourseHasReachedStudentLimit_ThrowsCourseOverflowException() {
        Course course = Course.builder()
                .courseId(1)
                .endDate(LocalDate.now().plusDays(1))
                .studentList(new ArrayList<>())
                .build();
        Student student = Student.builder()
                .studentId(1)
                .surname("surname")
                .name("name")
                .courseList(new ArrayList<>())
                .build();
        RegisterDto registerDto = RegisterDto.builder()
                .studentId(course.getCourseId())
                .courseId(student.getStudentId())
                .build();
        when(courseRepository.findById(registerDto.getCourseId())).thenReturn(Optional.of(course));
        when(studentRepository.findById(registerDto.getStudentId())).thenReturn(Optional.of(student));
        when(courseRepository.isCourseHaveTooManyStudents(registerDto.getCourseId(), courseCapacity)).thenReturn(true);

        Assertions.assertThrows(CapacityException.class, () -> courseService.registerStudentToCourse(registerDto));
    }

    @Test
    void isValid_StudentHasReachedCourseLimit_ThrowsCourseOverflowException() {
        Course course = Course.builder()
                .courseId(1)
                .endDate(LocalDate.now().plusDays(1))
                .studentList(new ArrayList<>())
                .build();
        Student student = Student.builder()
                .studentId(1)
                .surname("surname")
                .name("name")
                .courseList(new ArrayList<>())
                .build();
        RegisterDto registerDto = RegisterDto.builder()
                .studentId(course.getCourseId())
                .courseId(student.getStudentId())
                .build();
        when(courseRepository.findById(registerDto.getCourseId())).thenReturn(Optional.of(course));
        when(studentRepository.findById(registerDto.getStudentId())).thenReturn(Optional.of(student));
        when(studentRepository.isStudentHaveTooManyCourses(registerDto.getStudentId(), studentCapacity)).thenReturn(true);

        Assertions.assertThrows(CapacityException.class, () -> courseService.registerStudentToCourse(registerDto));
    }
    @Test
    void registerStudentToCourse_StudentAlreadyEnrolledCourse() {
        Course course = Course.builder()
                .courseId(1)
                .endDate(LocalDate.now().plusDays(1))
                .studentList(new ArrayList<>())
                .build();
        Student student = Student.builder()
                .studentId(1)
                .surname("surname")
                .name("name")
                .courseList(new ArrayList<>())
                .build();
        RegisterDto registerDto = RegisterDto.builder()
                .studentId(course.getCourseId())
                .courseId(student.getStudentId())
                .build();
        when(courseRepository.findById(registerDto.getCourseId())).thenReturn(Optional.of(course));
        when(studentRepository.findById(registerDto.getStudentId())).thenReturn(Optional.of(student));
        when(studentRepository.existByCourseId(student.getStudentId(), course.getCourseId())).thenReturn(true);

        Assertions.assertThrows(StudentAlreadyEnrolledException.class, () -> courseService.registerStudentToCourse(registerDto));
    }
}