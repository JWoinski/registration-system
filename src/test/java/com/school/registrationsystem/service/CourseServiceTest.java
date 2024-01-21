package com.school.registrationsystem.service;

import com.school.registrationsystem.model.Course;
import com.school.registrationsystem.model.Student;
import com.school.registrationsystem.model.dto.CourseDto;
import com.school.registrationsystem.model.dto.RegisterDto;
import com.school.registrationsystem.repository.CourseRepository;
import com.school.registrationsystem.repository.StudentRepository;
import com.school.registrationsystem.testData.CourseControllerTestData;
import com.school.registrationsystem.testData.StudentControllerTestData;
import com.school.registrationsystem.validator.capacity.course.CourseValidator;
import com.school.registrationsystem.validator.capacity.student.StudentValidator;
import com.school.registrationsystem.validator.registrationPeriod.CourseOpenValidator;
import com.school.registrationsystem.validator.studentEnrolled.StudentEnrolledValidator;
import jakarta.persistence.EntityNotFoundException;
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
    @Mock
    private StudentControllerTestData studentTest;
    @Mock
    private CourseControllerTestData courseTest;
    @InjectMocks
    private CourseOpenValidator courseOpenValidator;
    @InjectMocks
    private StudentValidator studentValidator;
    @InjectMocks
    private CourseValidator courseValidator;
    @InjectMocks
    private StudentEnrolledValidator studentEnrolledValidator;
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
        //TODO course startdate is after end date
//        assertThrows(ConstraintViolationException.class, () -> new CourseDto("name", LocalDate.now(), LocalDate.now().minusYears(1), "description"));
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

        Student student = new Student();
        student.setStudentId(1);
        student.setCourseList(new ArrayList<>());

        Course course = new Course();
        course.setCourseId(2);
        course.setStudentList(new ArrayList<>());

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


        boolean result = courseOpenValidator.isValid(registerDto, null);

        assertFalse(result);
        //TODO add following line
//        Assertions.assertThrows(ConstraintViolationException.class,() -> courseService.registerStudentToCourse(registerDto));
    }

    @Test
    void registerStudentToCourse_EndDateIsNotOutOfTime() {
        RegisterDto registerDto = RegisterDto.builder()
                .studentId(1)
                .courseId(1)
                .build();

        Course course = Course.builder()
                .endDate(LocalDate.now().plusDays(1))
                .build();
        when(courseRepository.findById(registerDto.getCourseId())).thenReturn(Optional.of(course));

        boolean result = courseOpenValidator.isValid(registerDto, null);

        assertTrue(result);
    }

    @Test
    void isValid_CourseHasReachedStudentLimit_ThrowsCourseOverflowException() {
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
        when(courseRepository.isCourseHaveNotSpecifiedStudents(registerDto.getCourseId(), courseCapacity)).thenReturn(false);

        assertFalse(courseValidator.isValid(registerDto, null));
        //TODO add following line
//        Assertions.assertThrows(ConstraintViolationException.class,() -> courseService.registerStudentToCourse(registerDto));
    }

    @Test
    void isValid_CourseHasNotReachedStudentLimit_ReturnsTrue() {
        Course course = Course.builder()
                .courseId(1)
                .endDate(LocalDate.now().minusDays(1))
                .build();
        RegisterDto registerDto = RegisterDto.builder()
                .studentId(1)
                .courseId(course.getCourseId())
                .build();
        when(courseRepository.isCourseHaveNotSpecifiedStudents(course.getCourseId(), courseCapacity)).thenReturn(true);

        assertTrue(courseValidator.isValid(registerDto, null));
    }

    @Test
    void isValid_StudentHasReachedCourseLimit_ThrowsCourseOverflowException() {
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
        when(studentRepository.isStudentHaveNotSpecifiedCourses(registerDto.getStudentId(), studentCapacity)).thenReturn(false);

        assertFalse(studentValidator.isValid(registerDto, null));
        //TODO add following line
//        Assertions.assertThrows(ConstraintViolationException.class,() -> courseService.registerStudentToCourse(registerDto));
    }

    @Test
    void isValid_StudentHasNotReachedCourseLimit_ReturnsTrue() {
        Course course = Course.builder()
                .courseId(1)
                .endDate(LocalDate.now().minusDays(1))
                .build();
        RegisterDto registerDto = RegisterDto.builder()
                .studentId(1)
                .courseId(course.getCourseId())
                .build();
        when(studentRepository.isStudentHaveNotSpecifiedCourses(registerDto.getStudentId(), studentCapacity)).thenReturn(true);

        assertTrue(studentValidator.isValid(registerDto, null));
    }

    @Test
    void registerStudentToCourse_StudentAlreadyEnrolledCourse() {
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
        when(studentRepository.existByCourseId(registerDto.getStudentId(), registerDto.getCourseId())).thenReturn(false);
        assertFalse(studentEnrolledValidator.isValid(registerDto, null));
        //TODO add following line
//        Assertions.assertThrows(ConstraintViolationException.class,() -> courseService.registerStudentToCourse(registerDto));
    }

    @Test
    void registerStudentToCourse_StudentNotEnrolledCourse() {
        RegisterDto registerDto = RegisterDto.builder()
                .studentId(1)
                .courseId(1)
                .build();
        when(studentRepository.existByCourseId(registerDto.getStudentId(), registerDto.getCourseId())).thenReturn(true);
        assertTrue(studentEnrolledValidator.isValid(registerDto, null));
    }
}