package com.school.registrationsystem.model.dto;

import com.school.registrationsystem.validator.capacity.course.HasCourseCapacity;
import com.school.registrationsystem.validator.capacity.student.HasStudentCapacity;
import com.school.registrationsystem.validator.registrationPeriod.IsCourseOpen;
import com.school.registrationsystem.validator.studentEnrolled.IsStudentAlreadyEnrolled;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@HasCourseCapacity
@HasStudentCapacity
@IsCourseOpen
@IsStudentAlreadyEnrolled
public class RegisterDto {
    private int studentId;
    private int courseId;
}
