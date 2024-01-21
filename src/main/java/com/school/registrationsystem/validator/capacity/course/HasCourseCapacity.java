package com.school.registrationsystem.validator.capacity.course;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CourseValidator.class)
public @interface HasCourseCapacity {
    String message() default "Course is overflowing.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
