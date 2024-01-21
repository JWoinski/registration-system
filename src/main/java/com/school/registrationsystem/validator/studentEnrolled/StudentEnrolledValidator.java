package com.school.registrationsystem.validator.studentEnrolled;

import com.school.registrationsystem.model.dto.RegisterDto;
import com.school.registrationsystem.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


@RequiredArgsConstructor
@Component
@Validated
public class StudentEnrolledValidator implements ConstraintValidator<IsStudentAlreadyEnrolled, RegisterDto> {
    private final StudentRepository studentRepository;

    @Override
    public void initialize(IsStudentAlreadyEnrolled constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    /**
     * Validates if a student registration is not enrolling second time.
     *
     * @param registerDto                The data transfer object containing registration information.
     * @param constraintValidatorContext The context in which the validation is performed.
     * @return True if the registration is valid; false otherwise.
     */
    @Override
    public boolean isValid(RegisterDto registerDto, ConstraintValidatorContext constraintValidatorContext) {
        return studentRepository.existByCourseId(registerDto.getStudentId(), registerDto.getCourseId());
    }
}
