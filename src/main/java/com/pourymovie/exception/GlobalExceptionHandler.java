package com.pourymovie.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(CustomException.class)
  public ProblemDetail handleCustomException(CustomException ex) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            ex.getStatus(),
            ex.getMessage()
    );
    problemDetail.setTitle(ex.getStatus().getReasonPhrase());
    problemDetail.setProperty("timestamp", Instant.now());
    return problemDetail;
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ProblemDetail handleValidationErrors(MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();

    ex.getBindingResult().getAllErrors().forEach(error -> {
      if (error instanceof FieldError) {
        String fieldName = ((FieldError) error).getField();
        String errorMessage = error.getDefaultMessage();
        errors.put(fieldName, errorMessage);
      } else {
        String objectName = error.getObjectName();
        String errorMessage = error.getDefaultMessage();
        errors.put(objectName, errorMessage);
      }
    });

    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST,
            "Validation failed"
    );
    problemDetail.setTitle("Bad Request");
    problemDetail.setProperty("timestamp", Instant.now());
    problemDetail.setProperty("errors", errors);
    return problemDetail;
  }


  @ExceptionHandler(AccessDeniedException.class)
  public ProblemDetail handleAccessDeniedException(AccessDeniedException ex) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.FORBIDDEN,
            "Access denied"
    );
    problemDetail.setTitle("Forbidden");
    problemDetail.setProperty("timestamp", Instant.now());
    return problemDetail;
  }

  @ExceptionHandler(AuthenticationException.class)
  public ProblemDetail handleAuthenticationException(AuthenticationException ex) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.UNAUTHORIZED,
            "Authentication failed"
    );
    problemDetail.setTitle("Unauthorized");
    problemDetail.setProperty("timestamp", Instant.now());
    return problemDetail;
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ProblemDetail handleDataIntegrityViolation(DataIntegrityViolationException ex) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.CONFLICT,
            "Database constraint violation"
    );
    problemDetail.setTitle("Conflict");
    problemDetail.setProperty("timestamp", Instant.now());
    String message = ex.getMostSpecificCause().getMessage();
    if (message.contains("unique constraint") || message.contains("Duplicate entry")) {
      problemDetail.setDetail("A record with this value already exists");
    }

    return problemDetail;
  }

  @ExceptionHandler(DataAccessException.class)
  public ProblemDetail handleDataAccessException(DataAccessException ex) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Database operation failed"
    );
    problemDetail.setTitle("Database Error");
    problemDetail.setProperty("timestamp", Instant.now());
    return problemDetail;
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ProblemDetail handleConstraintViolation(ConstraintViolationException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getConstraintViolations().forEach(violation -> {
      errors.put(violation.getPropertyPath().toString(), violation.getMessage());
    });

    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST,
            "Validation constraint violation"
    );
    problemDetail.setTitle("Bad Request");
    problemDetail.setProperty("timestamp", Instant.now());
    problemDetail.setProperty("errors", errors);
    return problemDetail;
  }

  @ExceptionHandler({Exception.class , RuntimeException.class})
  public ProblemDetail handleGlobalException(Exception ex) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Internal server error"
    );
    problemDetail.setTitle("Internal Server Error");
    problemDetail.setProperty("timestamp", Instant.now());
    problemDetail.setProperty("details", ex.getMessage());
    return problemDetail;
  }
}
