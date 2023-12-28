package com.school.registrationsystem.controller;

import com.school.registrationsystem.model.Course;
import com.school.registrationsystem.model.Student;
import com.school.registrationsystem.model.response.CourseResponse;
import com.school.registrationsystem.model.response.StudentResponse;
import com.school.registrationsystem.service.CourseService;
import com.school.registrationsystem.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/filters")
public class FilterController {
    private final CourseService courseService;
    private final StudentService studentService;

    @GetMapping("/courses/{courseIndex}")
    public ResponseEntity<StudentResponse> getStudentsByCourseIndex(@PathVariable int courseIndex) {
        List<Student> studentListByCourseIndex = studentService.getStudentListByCourseIndex(courseIndex);
        return new ResponseEntity<>(new StudentResponse(studentListByCourseIndex), HttpStatus.OK);
    }

    @GetMapping("/students/{studentIndex}")
    public ResponseEntity<CourseResponse> getCoursesByStudentIndex(@PathVariable int studentIndex) {
        List<Course> courseListByStudentIndex = courseService.getCourseListByStudentIndex(studentIndex);
        return new ResponseEntity<>(new CourseResponse(courseListByStudentIndex), HttpStatus.OK);
    }

    @GetMapping("/coursesWithNoStudents")
    public ResponseEntity<CourseResponse> getCoursesByEmptyStudentList() {
        List<Course> courseListByEmptyStudentList = courseService.getCourseListByEmptyStudentList();
        return new ResponseEntity<>(new CourseResponse(courseListByEmptyStudentList), HttpStatus.OK);
    }

    @GetMapping("/studentsWithNoCourses")
    public ResponseEntity<StudentResponse> getStudentsByEmptyCourseList() {
        List<Student> studentByEmptyCourseList = studentService.getStudentByEmptyCourseList();
        return new ResponseEntity<>(new StudentResponse(studentByEmptyCourseList), HttpStatus.OK);
    }
}
