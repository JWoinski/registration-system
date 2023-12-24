package com.school.registrationsystem.controller;

import com.school.registrationsystem.model.dto.StudentDto;
import com.school.registrationsystem.model.response.ApiResponse;
import com.school.registrationsystem.model.response.StudentResponse;
import com.school.registrationsystem.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/students")
public class StudentController {
    private final StudentService studentService;

    @PostMapping
    public ResponseEntity<ApiResponse> saveCourse(@RequestBody StudentDto studentDto) {
        studentService.saveStudent(studentDto);
        return new ResponseEntity<>(new ApiResponse("Student saved correctly"), HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse> deleteCourse(@RequestBody int studentIndex) {
        studentService.deleteStudent(studentIndex);
        return new ResponseEntity<>(new ApiResponse("Student deleted correctly"), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<ApiResponse> modifyCourse(@RequestBody StudentDto studentDto) {
        studentService.modifyStudent(studentDto);
        return new ResponseEntity<>(new ApiResponse("Student modified correctly"), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<StudentResponse> getCourses() {
        return new ResponseEntity<>(new StudentResponse(studentService.getAll()), HttpStatus.OK);
    }
}
