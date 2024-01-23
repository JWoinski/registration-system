package com.school.registrationsystem.validator.capacity.course;

import com.school.registrationsystem.model.dto.RegisterDto;
import com.school.registrationsystem.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
@Validated
@RequiredArgsConstructor
public class CourseValidator implements ConstraintValidator<HasCourseCapacity, RegisterDto> {
    private final CourseRepository courseRepository;
    @Value("${course.students.threshold}")
    private int courseCapacity;

    @Override
    public void initialize(HasCourseCapacity constraintAnnotation) {
    }

    /**
     * Checks if a course has less than specified students enrolled.
     *
     * @param registerDto The studentId of the registerDto for whom the course enrollment is checked.
     * @return true if the course has less than specified students, false otherwise.
     */
    @Override
    public boolean isValid(RegisterDto registerDto, ConstraintValidatorContext constraintValidatorContext) {
        return courseRepository.isCourseHaveTooManyStudents(registerDto.getCourseId(), courseCapacity);
    }
}
