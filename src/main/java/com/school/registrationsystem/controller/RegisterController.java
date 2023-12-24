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

@RestController
@RequiredArgsConstructor
@RequestMapping("/register")
public class RegisterController {
    private final StudentService studentService;

    @PostMapping
    public ResponseEntity<ApiResponse> registerToCourse(@RequestBody RegisterDto registerDto) {
        studentService.registerStudentToCourse(registerDto);
        return new ResponseEntity<>(new ApiResponse("Student registered correctly"), HttpStatus.OK);
    }

}
