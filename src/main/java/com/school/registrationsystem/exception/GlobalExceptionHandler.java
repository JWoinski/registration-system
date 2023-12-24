package com.school.registrationsystem.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class GlobalExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiError> handleEntityNotFoundException(EntityNotFoundException e) {
        return new ResponseEntity<>(new ApiError(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CapacityException.class)
    public ResponseEntity<ApiError> handleCapacityException(CapacityException e) {
        return new ResponseEntity<>(new ApiError(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IndexOccupiedException.class)
    public ResponseEntity<ApiError> handleIndexOccupiedException(CapacityException e) {
        return new ResponseEntity<>(new ApiError(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateEnrollmentException.class)
    public ResponseEntity<ApiError> handleDuplicateEnrollmentException(CapacityException e) {
        return new ResponseEntity<>(new ApiError(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TimeOutCourseRegisterException.class)
    public ResponseEntity<ApiError> handleTimeOutCourseRegisterException(CapacityException e) {
        return new ResponseEntity<>(new ApiError(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
