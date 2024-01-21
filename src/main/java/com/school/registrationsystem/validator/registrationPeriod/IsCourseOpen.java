package com.school.registrationsystem.validator.registrationPeriod;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CourseOpenValidator.class)
public @interface IsCourseOpen {
    String message() default "Course has been closed.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
