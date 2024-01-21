package com.school.registrationsystem.controller;

import com.school.registrationsystem.model.Course;
import com.school.registrationsystem.model.dto.CourseDto;
import com.school.registrationsystem.model.dto.RegisterDto;
import com.school.registrationsystem.model.response.ApiResponse;
import com.school.registrationsystem.service.CourseService;
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

import javax.validation.Valid;

/**
 * Controller handling operations related to courses.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/courses")
@Validated
@ControllerAdvice
public class CourseController {
    private final CourseService courseService;

    @Operation(
            summary = "Save a new course.",
            description = "Save a new course by specifying course dto.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = ApiResponse.class), examples = @ExampleObject(value = "{\"message\": \"Course saved correctly.\"}"), mediaType = "application/json")}, description = "Course saved correctly."),
    })
    @PostMapping
    public ResponseEntity<ApiResponse> saveCourse(@Valid @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"name\": \"Example Course\",\"startDate\": \"2022-01-01\",\"endDate\": \"2022-02-01\",\"description\": \"Example Description\"}")))
                                                  CourseDto courseDto) {
        courseService.saveCourse(courseDto);
        return new ResponseEntity<>(new ApiResponse("Course saved correctly."), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Delete a course.",
            description = "Delete a course by its id.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = ApiResponse.class), examples = @ExampleObject(value = "{\"message\": \"Course deleted correctly.\"}"), mediaType = "application/json")}, description = "Course deleted correctly."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())}, description = "Course not found."),
    })
    @DeleteMapping
    public ResponseEntity<ApiResponse> deleteCourse(@RequestParam Integer courseId) {
        courseService.deleteCourse(courseId);
        return new ResponseEntity<>(new ApiResponse("Course deleted correctly."), HttpStatus.OK);
    }

    @Operation(
            summary = "Modify a course.",
            description = "Modify a course by its id and specified new course dto.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = ApiResponse.class), examples = @ExampleObject(value = "{\"message\": \"Course modified correctly.\"}"), mediaType = "application/json")}, description = "Course modified correctly."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())}, description = "Course not found."),})
    @PutMapping
    public ResponseEntity<ApiResponse> modifyCourse(@RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"name\": \"Example Course\",\"startDate\": \"2022-01-01\",\"endDate\": \"2022-02-01\",\"description\": \"Example Description\"}")))
                                                    CourseDto courseDto, @RequestParam int courseId) {
        courseService.modifyCourse(courseDto, courseId);
        return new ResponseEntity<>(new ApiResponse("Course modified correctly."), HttpStatus.OK);
    }

    @Operation(
            summary = "Get courses.",
            description = "Get all courses/ by enrolled studentId / without enrolled students.")
    @GetMapping
    public ResponseEntity<Page<Course>> getCourses(
            @RequestParam(required = false) Integer studentId,
            @RequestParam(required = false) Boolean withoutStudents,
            Pageable pageable) {
        Page<Course> courses = courseService.getAll(studentId, withoutStudents, pageable);
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @Operation(
            summary = "Register student to course.",
            description = "Register student to course by ids of course and student.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = ApiResponse.class), examples = @ExampleObject(value = "{\"message\": \"Student registered correctly.\"}"), mediaType = "application/json")}),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())}, description = "Course/student not found."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", content = {@Content(schema = @Schema())}),})
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerToCourse(@RequestBody RegisterDto registerDto) {
        courseService.registerStudentToCourse(registerDto);
        return new ResponseEntity<>(new ApiResponse("Student registered correctly"), HttpStatus.OK);
    }
}
