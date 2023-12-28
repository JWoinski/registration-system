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

@RestController
@RequiredArgsConstructor
@RequestMapping("/courses")
public class CourseController {
    private final CourseService courseService;

    @PostMapping
    public ResponseEntity<ApiResponse> saveCourse(@RequestBody CourseDto courseDto) {
        courseService.saveCourse(courseDto);
        return new ResponseEntity<>(new ApiResponse("Course saved correctly"), HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse> deleteCourse(@RequestBody IndexRequest indexRequest) {
        courseService.deleteCourse(indexRequest.getIndex());
        return new ResponseEntity<>(new ApiResponse("Course deleted correctly"), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<ApiResponse> modifyCourse(@RequestBody CourseDto courseDto) {
        courseService.modifyCourse(courseDto);
        return new ResponseEntity<>(new ApiResponse("Course modified correctly"), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<CourseResponse> getCourses() {
        return new ResponseEntity<>(new CourseResponse(courseService.getAll()), HttpStatus.OK);
    }
}
