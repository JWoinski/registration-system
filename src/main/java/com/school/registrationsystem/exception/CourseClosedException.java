package com.school.registrationsystem.exception;

/**
 * Exception thrown when a capacity limit is reached, such as a course being full.
 */
public class CourseClosedException extends RuntimeException {
    public CourseClosedException(String message) {
        super(message);
    }
}
