package com.school.registrationsystem.validator.registrationPeriod;

import com.school.registrationsystem.model.Course;
import com.school.registrationsystem.model.dto.RegisterDto;
import com.school.registrationsystem.repository.CourseRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.util.Optional;

@Component
@Validated
@RequiredArgsConstructor
public class CourseOpenValidator implements ConstraintValidator<IsCourseOpen, RegisterDto> {
    private final CourseRepository courseRepository;

    @Override
    public void initialize(IsCourseOpen constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    /**
     * Validates if a registration date is before course end date.
     *
     * @param registerDto                The data transfer object containing registration information.
     * @param constraintValidatorContext The context in which the validation is performed.
     * @return True if the registration is valid; false otherwise.
     * @throws EntityNotFoundException If the course with the specified ID is not found.
     */
    @Override
    public boolean isValid(RegisterDto registerDto, ConstraintValidatorContext constraintValidatorContext) {
        Optional<Course> course = Optional.ofNullable(courseRepository.findById(registerDto.getCourseId())
                .orElseThrow(() -> new EntityNotFoundException("Course not found.")));
        return course.map(value -> value.getEndDate().isAfter(LocalDate.now())).orElse(false);
    }
}
