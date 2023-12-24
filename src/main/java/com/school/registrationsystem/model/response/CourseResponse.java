package com.school.registrationsystem.model.response;

import com.school.registrationsystem.model.Course;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseResponse {
    List<Course> courseList;
}
