package com.school.registrationsystem.exception;

/**
 * Exception thrown when a capacity limit is reached, such as a course being full.
 */
public class CapacityException extends RuntimeException {
    public CapacityException(String s) {
        super(s);
    }
}
