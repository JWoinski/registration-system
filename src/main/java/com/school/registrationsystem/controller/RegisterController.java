package com.school.registrationsystem.controller;

import com.school.registrationsystem.model.dto.RegisterDto;
import com.school.registrationsystem.model.response.ApiResponse;
import com.school.registrationsystem.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller handling student registration to courses.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/register")
public class RegisterController {
    private final StudentService studentService;

    /**
     * Registers a student to a course.
     *
     * @param registerDto The details for registering a student to a course.
     * @return ResponseEntity containing ApiResponse with the result message.
     */
    @PostMapping
    public ResponseEntity<ApiResponse> registerToCourse(@RequestBody RegisterDto registerDto) {
        studentService.registerStudentToCourse(registerDto);
        return new ResponseEntity<>(new ApiResponse("Student registered correctly"), HttpStatus.OK);
    }

}
