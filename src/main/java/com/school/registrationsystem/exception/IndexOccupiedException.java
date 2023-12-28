package com.school.registrationsystem.exception;

/**
 * Exception thrown when attempting to save an entity with an index that is already occupied.
 * This usually happens when trying to save a student or course with an index that is already in use.
 */
public class IndexOccupiedException extends RuntimeException {
    public IndexOccupiedException(String s) {
        super(s);
    }
}
