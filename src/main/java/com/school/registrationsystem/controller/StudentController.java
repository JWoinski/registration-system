package com.school.registrationsystem.controller;

import com.school.registrationsystem.model.Student;
import com.school.registrationsystem.model.dto.StudentDto;
import com.school.registrationsystem.model.response.ApiResponse;
import com.school.registrationsystem.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Controller handling operations related to students.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/students")
@Validated
public class StudentController {
    private final StudentService studentService;

    @Operation(
            summary = "Save a new student.",
            description = "Save a new student by specyfing student dto.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = ApiResponse.class), examples = @ExampleObject(value = "{\"message\": \"Student saved correctly.\"}"), mediaType = "application/json")}, description = "Student saved correctly."),})
    @PostMapping
    public ResponseEntity<ApiResponse> saveStudent(@RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"name\": \"Example name\",\"surname\": \"Example surname\"}"))) StudentDto studentDto) {
        studentService.saveStudent(studentDto);
        return new ResponseEntity<>(new ApiResponse("Student saved correctly"), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Delete a student.",
            description = "Delete a student by its id.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = ApiResponse.class), examples = @ExampleObject(value = "{\"message\": \"Student deleted correctly.\"}"), mediaType = "application/json")}, description = "Student deleted correctly."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())}, description = "Student not found."),})
    @DeleteMapping
    public ResponseEntity<ApiResponse> deleteStudent(@RequestParam Integer studentId) {
        studentService.deleteStudent(studentId);
        return new ResponseEntity<>(new ApiResponse("Student deleted correctly"), HttpStatus.OK);
    }

    @Operation(
            summary = "Modify a student.",
            description = "Modify a student by its id and specified new student dto.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = ApiResponse.class), examples = @ExampleObject(value = "{\"message\": \"Student modified correctly.\"}"), mediaType = "application/json")}, description = "Student modified correctly."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())}, description = "Student not found."),})
    @PutMapping
    public ResponseEntity<ApiResponse> modifyStudent(@RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"name\": \"Example name\",\"surname\": \"Example surname\"}"))) StudentDto studentDto, @RequestParam int studentId) {
        studentService.modifyStudent(studentDto, studentId);
        return new ResponseEntity<>(new ApiResponse("Student modified correctly"), HttpStatus.OK);
    }

    @Operation(
            summary = "Get students.",
            description = "Get all students/ by enroled courseId / without enrolled courses.")
    @GetMapping
    public ResponseEntity<Page<Student>> getStudents(@RequestParam(required = false) Integer courseId,
                                                     @RequestParam(required = false) Boolean withoutCourses,
                                                     Pageable pageable) {
        return new ResponseEntity<>(studentService.getAll(courseId, withoutCourses, pageable), HttpStatus.OK);
    }
}
