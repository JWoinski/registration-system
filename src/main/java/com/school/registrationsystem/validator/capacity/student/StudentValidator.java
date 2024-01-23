package com.school.registrationsystem.validator.capacity.student;

import com.school.registrationsystem.model.dto.RegisterDto;
import com.school.registrationsystem.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
@RequiredArgsConstructor
public class StudentValidator implements ConstraintValidator<HasStudentCapacity, RegisterDto> {
    private final StudentRepository studentRepository;
    @Value("${student.courses.threshold}")
    private int studentCapacity;

    /**
     * Checks if a student is enrolled in less than 5 courses.
     *
     * @param registerDto The courseId of the registerDto for which the student's course enrollment is checked.
     * @return true if the student is enrolled in less than 5 courses, false otherwise.
     */

    @Override
    public boolean isValid(RegisterDto registerDto, ConstraintValidatorContext constraintValidatorContext) {
        return studentRepository.isStudentHaveTooManyCourses(registerDto.getStudentId(), studentCapacity);
    }
}
