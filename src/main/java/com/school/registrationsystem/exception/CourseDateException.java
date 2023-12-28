package com.school.registrationsystem.exception;

/**
 * Exception thrown when there is an issue with course dates, such as an invalid start or end date.
 */
public class CourseDateException extends RuntimeException {
    public CourseDateException(String s) {
        super(s);
    }
}
