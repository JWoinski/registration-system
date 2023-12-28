package com.school.registrationsystem.exception;

/**
 * Exception thrown when attempting to register for a course after the registration deadline has passed.
 * This occurs when a student tries to enroll in a course that is no longer open for registration.
 */
public class TimeOutCourseRegisterException extends RuntimeException {
    public TimeOutCourseRegisterException(String s) {
        super(s);
    }
}
