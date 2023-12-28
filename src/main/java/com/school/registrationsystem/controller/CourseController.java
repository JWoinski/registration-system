package com.school.registrationsystem.controller;

import com.school.registrationsystem.model.dto.CourseDto;
import com.school.registrationsystem.model.request.IndexRequest;
import com.school.registrationsystem.model.response.ApiResponse;
import com.school.registrationsystem.model.response.CourseResponse;
import com.school.registrationsystem.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller handling operations related to courses.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/courses")
public class CourseController {
    private final CourseService courseService;

    /**
     * Saves a new course.
     *
     * @param courseDto The details of the new course.
     * @return ResponseEntity containing ApiResponse with the result message.
     */
    @PostMapping
    public ResponseEntity<ApiResponse> saveCourse(@RequestBody CourseDto courseDto) {
        courseService.saveCourse(courseDto);
        return new ResponseEntity<>(new ApiResponse("Course saved correctly"), HttpStatus.CREATED);
    }

    /**
     * Deletes a course.
     *
     * @param indexRequest The request containing the index of the course to be deleted.
     * @return ResponseEntity containing ApiResponse with the result message.
     */
    @DeleteMapping
    public ResponseEntity<ApiResponse> deleteCourse(@RequestBody IndexRequest indexRequest) {
        courseService.deleteCourse(indexRequest.getIndex());
        return new ResponseEntity<>(new ApiResponse("Course deleted correctly"), HttpStatus.OK);
    }

    /**
     * Modifies an existing course.
     *
     * @param courseDto The modified details of the course.
     * @return ResponseEntity containing ApiResponse with the result message.
     */
    @PutMapping
    public ResponseEntity<ApiResponse> modifyCourse(@RequestBody CourseDto courseDto) {
        courseService.modifyCourse(courseDto);
        return new ResponseEntity<>(new ApiResponse("Course modified correctly"), HttpStatus.OK);
    }

    /**
     * Retrieves a list of all courses.
     *
     * @return ResponseEntity containing CourseResponse with the list of courses.
     */
    @GetMapping
    public ResponseEntity<CourseResponse> getCourses() {
        return new ResponseEntity<>(new CourseResponse(courseService.getAll()), HttpStatus.OK);
    }
}
