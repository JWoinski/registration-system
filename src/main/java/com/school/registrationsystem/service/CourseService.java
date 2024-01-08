package com.school.registrationsystem.service;

import com.school.registrationsystem.exception.CourseDateException;
import com.school.registrationsystem.model.Course;
import com.school.registrationsystem.model.Student;
import com.school.registrationsystem.model.dto.CourseDto;
import com.school.registrationsystem.repository.CourseRepository;
import com.school.registrationsystem.repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;

    /**
     * Saves a new course based on the information provided in the CourseDto object.
     * Validates the course date before saving.
     *
     * @param courseDto Object containing course details used to create a new Course object.
     */
    public void saveCourse(CourseDto courseDto) {
        if (validCourseDto(courseDto)) {
            throw new NullPointerException("Course form is incorrect.");
        }
        //Create Course from CourseDto details
        Course course = Course.builder()
                .name(courseDto.getName())
                .courseIndex(courseDto.getCourseIndex())
                .startDate(courseDto.getStartDate())
                .endDate(courseDto.getEndDate())
                .description(courseDto.getDescription())
                .build();
        //Validate the date before save
        validateDate(course);

        //Save te course
        courseRepository.save(
                course);
    }

    /**
     * Validates the date range of a course to ensure the start date is not after the end date.
     * Throws a CourseDateException if the validation fails.
     *
     * @param course The Course object to be validated.
     * @throws CourseDateException Thrown if the start date is after the end date.
     */
    private void validateDate(Course course) {
        if (course.getStartDate().isAfter(course.getEndDate())) {
            throw new CourseDateException("Start date is after end date.");
        }
    }

    /**
     * Saves a Course object after validating its date and then persists it to the repository.
     *
     * @param course The Course object to be saved.
     * @throws CourseDateException Thrown if the validation of the course date fails.
     */
    public void saveCourse(Course course) {
        validateDate(course);
        courseRepository.save(course);
    }

    /**
     * Deletes a course by its course index, ensuring proper handling of associated students' course lists.
     * Uses a transaction to maintain data consistency.
     *
     * @param courseIndex The index of the course to be deleted.
     */
    @Transactional
    public void deleteCourse(int courseIndex) {
        Course byCourseIndex = courseRepository.findByCourseIndex(courseIndex);
        if (byCourseIndex != null) {
            for (Student student : byCourseIndex.getStudentList()) {
                student.getCourseList().remove(byCourseIndex);
                studentRepository.save(student);
            }
        }
        courseRepository.deleteByCourseIndex(courseIndex);
    }

    /**
     * Modifies a course based on the information provided in the CourseDto, updating its values and validating the date.
     *
     * @param courseDto The CourseDto containing the modified course details.
     * @throws NullPointerException    Thrown if the modification form is incorrect.
     * @throws EntityNotFoundException Thrown if the course with the specified index is not found.
     */
    public void modifyCourse(CourseDto courseDto) {
        if (validCourseDto(courseDto)) {
            throw new NullPointerException("Modification form is incorrect.");
        }

        //Retrieve the existing course by its index
        Course course = getCourseByCourseIndex(courseDto.getCourseIndex());
        if (course == null) {
            throw new EntityNotFoundException("Course has been not found.");
        }

        //update the valuse of the existing course with those from courseDto
        updateCourseValues(course, courseDto);

        //validate the modified course date
        validateDate(course);

        //save the modified course to the repository
        courseRepository.save(course);
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
    }

    /**
     * Retrieves a list of all courses from the repository.
     *
     * @return A List of Course objects representing all courses in the repository.
     */
    public List<Course> getAll() {
        return courseRepository.findAll();
    }

    /**
     * Retrieves a list of courses associated with a student based on the provided student index.
     *
     * @param studentIndex The index of the student for whom the associated courses are to be retrieved.
     * @return A List of Course objects representing the courses associated with the specified student.
     */
    public List<Course> getCourseListByStudentIndex(int studentIndex) {
        return courseRepository.findByStudentIndex(studentIndex);
    }

    /**
     * Retrieves a list of courses with an empty associated student list.
     *
     * @return A List of Course objects representing courses without any associated students.
     */
    public List<Course> getCourseListByEmptyStudentList() {
        return courseRepository.findByEmptyStudentList();
    }

    /**
     * Retrieves a course based on the provided course index.
     *
     * @param courseIndex The index of the course to be retrieved.
     * @return The Course object representing the course with the specified index, or null if not found.
     */
    public Course getCourseByCourseIndex(int courseIndex) {
        return courseRepository.findByCourseIndex(courseIndex);
    }

    /**
     * Saves a list of courses to the repository.
     *
     * @param courses The List of Course objects to be saved.
     */
    public void saveAll(List<Course> courses) {
        courseRepository.saveAll(courses);
    }

    /**
     * Checks whether a CourseDto is valid by verifying that essential properties are not null or default.
     *
     * @param courseDto The CourseDto to be validated.
     * @return true if the CourseDto is considered invalid, false otherwise.
     */

    private boolean validCourseDto(CourseDto courseDto) {
        return courseDto.getName() == null || courseDto.getStartDate() == null || courseDto.getEndDate() == null || courseDto.getDescription() == null || courseDto.getCourseIndex() == 0;
    }
}