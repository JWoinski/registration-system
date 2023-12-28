package com.school.registrationsystem.controller;

import com.school.registrationsystem.model.dto.StudentDto;
import com.school.registrationsystem.model.request.IndexRequest;
import com.school.registrationsystem.model.response.ApiResponse;
import com.school.registrationsystem.model.response.StudentResponse;
import com.school.registrationsystem.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller handling operations related to students.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/students")
public class StudentController {
    private final StudentService studentService;

    /**
     * Saves a new student.
     *
     * @param studentDto The details of the new student.
     * @return ResponseEntity containing ApiResponse with the result message.
     */
    @PostMapping
    public ResponseEntity<ApiResponse> saveCourse(@RequestBody StudentDto studentDto) {
        studentService.saveStudent(studentDto);
        return new ResponseEntity<>(new ApiResponse("Student saved correctly"), HttpStatus.CREATED);
    }

    /**
     * Deletes a student.
     *
     * @param studentIndex The request containing the index of the student to be deleted.
     * @return ResponseEntity containing ApiResponse with the result message.
     */
    @DeleteMapping
    public ResponseEntity<ApiResponse> deleteCourse(@RequestBody IndexRequest studentIndex) {
        studentService.deleteStudent(studentIndex.getIndex());
        return new ResponseEntity<>(new ApiResponse("Student deleted correctly"), HttpStatus.OK);
    }

    /**
     * Modifies an existing student.
     *
     * @param studentDto The modified details of the student.
     * @return ResponseEntity containing ApiResponse with the result message.
     */
    @PutMapping
    public ResponseEntity<ApiResponse> modifyCourse(@RequestBody StudentDto studentDto) {
        studentService.modifyStudent(studentDto);
        return new ResponseEntity<>(new ApiResponse("Student modified correctly"), HttpStatus.OK);
    }

    /**
     * Retrieves a list of all students.
     *
     * @return ResponseEntity containing StudentResponse with the list of students.
     */
    @GetMapping
    public ResponseEntity<StudentResponse> getCourses() {
        return new ResponseEntity<>(new StudentResponse(studentService.getAll()), HttpStatus.OK);
    }
}
