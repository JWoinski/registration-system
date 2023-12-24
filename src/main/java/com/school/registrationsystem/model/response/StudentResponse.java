package com.school.registrationsystem.model.response;

import com.school.registrationsystem.model.Student;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class StudentResponse {
    List<Student> studentList;
}
