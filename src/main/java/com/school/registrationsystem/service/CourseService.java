package com.school.registrationsystem.service;

import com.school.registrationsystem.exception.CourseDateException;
import com.school.registrationsystem.model.Course;
import com.school.registrationsystem.model.Student;
import com.school.registrationsystem.model.dto.CourseDto;
import com.school.registrationsystem.repository.CourseRepostiory;
import com.school.registrationsystem.repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepostiory courseRepostiory;
    private final StudentRepository studentRepository;

    public void saveCourse(CourseDto courseDto) {
        Course course = Course.builder()
                .name(courseDto.getName())
                .courseIndex(courseDto.getCourseIndex())
                .startDate(courseDto.getStartDate())
                .endDate(courseDto.getEndDate())
                .description(courseDto.getDescription())
                .build();

        validateDate(course);

        courseRepostiory.save(
                course);
    }

    private void validateDate(Course course) {
        if (course.getStartDate().isAfter(course.getEndDate())) {
            throw new CourseDateException("Start date is after end date.");
        }
    }

    public void saveCourse(Course course) {
        validateDate(course);
        courseRepostiory.save(course);
    }

    @Transactional
    public void deleteCourse(int courseIndex) {
        Course byCourseIndex = courseRepostiory.findByCourseIndex(courseIndex);
        if (byCourseIndex != null) {
            for (Student student : byCourseIndex.getStudentList()) {
                student.getCourseList().remove(byCourseIndex);
                studentRepository.save(student);
            }
        }
        courseRepostiory.deleteByCourseIndex(courseIndex);
    }

    public void modifyCourse(CourseDto courseDto) {
        if (validCourseDto(courseDto)) {
            throw new NullPointerException("Modification form is incorrect.");
        }
        Course course = getCourseByCourseIndex(courseDto.getCourseIndex());
        if (course == null) {
            throw new EntityNotFoundException("Course has been not found.");
        }
        updateCourseValues(course, courseDto);
        validateDate(course);
        courseRepostiory.save(course);
    }

    private void updateCourseValues(Course course, CourseDto courseDto) {
        course.setName(courseDto.getName());
        course.setStartDate(courseDto.getStartDate());
        course.setEndDate(courseDto.getEndDate());
        course.setDescription(courseDto.getDescription());
    }

    public List<Course> getAll() {
        return courseRepostiory.findAll();
    }

    public List<Course> getCourseListByStudentIndex(int studentIndex) {
        return courseRepostiory.findByStudentIndex(studentIndex);
    }

    public List<Course> getCourseListByEmptyStudentList() {
        return courseRepostiory.findByEmptyStudentList();
    }

    public Course getCourseByCourseIndex(int courseIndex) {
        return courseRepostiory.findByCourseIndex(courseIndex);
    }

    public void saveAll(List<Course> courses) {
        courseRepostiory.saveAll(courses);
    }

    private boolean validCourseDto(CourseDto courseDto) {
        return courseDto.getName() == null || courseDto.getStartDate() == null || courseDto.getEndDate() == null || courseDto.getDescription() == null || courseDto.getCourseIndex() == 0;
    }
}