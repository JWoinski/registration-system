package com.school.registrationsystem.exception;

/**
 * Exception thrown when attempting to enroll a student in a course where the student is already enrolled.
 */
public class DuplicateEnrollmentException extends RuntimeException {
    public DuplicateEnrollmentException(String s) {
        super(s);
    }
}
