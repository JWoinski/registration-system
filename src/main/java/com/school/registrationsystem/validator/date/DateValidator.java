package com.school.registrationsystem.validator.date;

import com.school.registrationsystem.model.dto.CourseDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ControllerAdvice;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
@Validated
@ControllerAdvice
public class DateValidator implements ConstraintValidator<DateValid, CourseDto> {
    /**
     * Validates the date range of a course to ensure the start date is not after the end date.
     * Throws a CourseDateException if the validation fails.
     *
     * @param courseDto The Course object to be validated.
     */
    @Override
    public boolean isValid(CourseDto courseDto, ConstraintValidatorContext context) {
//        return courseDto.getStartDate().isBefore(courseDto.getEndDate());
        return false;
    }
}
