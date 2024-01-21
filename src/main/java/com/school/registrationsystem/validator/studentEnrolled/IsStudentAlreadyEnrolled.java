package com.school.registrationsystem.validator.studentEnrolled;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StudentEnrolledValidator.class)
public @interface IsStudentAlreadyEnrolled {
    String message() default "Student is already on this course.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
