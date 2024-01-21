package com.school.registrationsystem.service;

import com.school.registrationsystem.model.Course;
import com.school.registrationsystem.model.Student;
import com.school.registrationsystem.model.dto.CourseDto;
import com.school.registrationsystem.model.dto.RegisterDto;
import com.school.registrationsystem.model.specification.CourseSpecification;
import com.school.registrationsystem.repository.CourseRepository;
import com.school.registrationsystem.repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Validated
public class CourseService {
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;

    /**
     * Saves a new course based on the information provided in the CourseDto object.
     * Validates the course date before saving.
     *
     * @param courseDto Object containing course details used to create a new Course object.
     */
    public void saveCourse(@Valid CourseDto courseDto) {
        CourseDto coursedto = new CourseDto(courseDto.getName(), courseDto.getStartDate(), courseDto.getEndDate(), courseDto.getDescription());
        Course course = Course.builder()
                .name(courseDto.getName())
                .startDate(courseDto.getStartDate())
                .endDate(courseDto.getEndDate())
                .description(courseDto.getDescription())
                .build();
        courseRepository.save(course);
    }

    /**
     * Deletes a course by its course index, ensuring proper handling of associated students' course lists.
     * Uses a transaction to maintain data consistency.
     *
     * @param courseId The id of the course to be deleted.
     */
    @Transactional
    public void deleteCourse(int courseId) {
        Optional<Course> course = Optional.ofNullable(courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course has been not found.")));
        if (course.isPresent()) {
            for (Student student : course.get().getStudentList()) {
                student.getCourseList().remove(course.get());
                studentRepository.save(student);
            }
        }
        courseRepository.deleteById(courseId);
    }

    /**
     * Modifies a course based on the information provided in the CourseDto, updating its values and validating the date.
     *
     * @param courseDto The CourseDto containing the modified course details.
     * @throws NullPointerException    Thrown if the modification form is incorrect.
     * @throws EntityNotFoundException Thrown if the course with the specified index is not found.
     */
    public void modifyCourse(CourseDto courseDto, int courseId) {
        Optional<Course> courseOptional = Optional.ofNullable(
                courseRepository.findById(courseId)
                        .orElseThrow(() -> new EntityNotFoundException("Course has been not found.")));
        courseOptional.ifPresent(course -> updateCourseValues(course, courseDto));
    }

    /**
     * Updates the values of an existing Course object with the information provided in the CourseDto.
     *
     * @param course    The existing Course object to be updated.
     * @param courseDto The CourseDto containing the modified course details.
     */
    private void updateCourseValues(Course course, CourseDto courseDto) {
        course.setName(courseDto.getName());
        course.setStartDate(courseDto.getStartDate());
        course.setEndDate(courseDto.getEndDate());
        course.setDescription(courseDto.getDescription());

        courseRepository.save(course);
    }

    /**
     * Retrieves a list of all courses from the repository.
     *
     * @param studentId       The id of the student to get filtered courses who are enrolled by student id.
     * @param withoutStudents If true, filters out courses that have no enrolled students.
     * @param pageable        Object used for pagination, specifying the page size, current page, etc.
     * @return A Page of Course objects representing courses in the repository based on the specified filters.
     */
    public Page<Course> getAll(Integer studentId, Boolean withoutStudents, Pageable pageable) {
        Specification<Course> specification = CourseSpecification.getSpecification(studentId, withoutStudents);
        return courseRepository.findAll(specification, pageable);
    }

    /**
     * Registers a student to a course based on the information provided in the RegisterDto.
     * Uses a transaction to ensure data consistency.
     *
     * @param registerDto The RegisterDto containing the details for registering a student to a course.
     */
    @Transactional
    public void registerStudentToCourse(RegisterDto registerDto) {
        //Retrieve the student and course based on the provided indices
        Optional<Student> studentOptional = Optional.ofNullable(
                studentRepository.findById(registerDto.getStudentId())
                        .orElseThrow(() -> new EntityNotFoundException("Student not found.")));
        Optional<Course> courseOptional = Optional.ofNullable(
                courseRepository.findById(registerDto.getCourseId())
                        .orElseThrow(() -> new EntityNotFoundException("Course not found.")));

        if (studentOptional.isPresent() && courseOptional.isPresent()) {
            Student student = studentOptional.get();
            Course course = courseOptional.get();

            //Add the student to the course's student list and vice versa
            student.getCourseList().add(course);
            course.getStudentList().add(student);

            //Save the updated student information to the repostiory
            studentRepository.save(student);
        }
    }
}