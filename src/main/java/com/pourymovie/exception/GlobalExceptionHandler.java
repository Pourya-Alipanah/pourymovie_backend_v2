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
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ResponseStatusException.class)
  public ProblemDetail handleResponseStatusException(ResponseStatusException ex) {
    return ex.getBody();
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
    return problemDetail;
  }

  @ExceptionHandler(AuthenticationException.class)
  public ProblemDetail handleAuthenticationException(AuthenticationException ex) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.UNAUTHORIZED,
            "Authentication failed"
    );
    problemDetail.setTitle("Unauthorized");
    return problemDetail;
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ProblemDetail handleDataIntegrityViolation(DataIntegrityViolationException ex) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.CONFLICT,
            "Database constraint violation"
    );
    problemDetail.setTitle("Conflict");
    String message = ex.getMostSpecificCause().getMessage();

    if (message.contains("unique constraint") || message.contains("Duplicate entry")) {
      problemDetail.setDetail("A record with this value already exists");
      problemDetail.setProperty("description", message);
      return problemDetail;
    }

    problemDetail.setDetail(message);

    return problemDetail;
  }

  @ExceptionHandler(DataAccessException.class)
  public ProblemDetail handleDataAccessException(DataAccessException ex) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Database operation failed"
    );
    String message = ex.getMostSpecificCause().getMessage();
    problemDetail.setTitle("Database Error");
    problemDetail.setDetail(message);
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
            "Database Validation constraint violation"
    );
    problemDetail.setTitle("Bad Request");
    problemDetail.setProperty("errors", errors);
    return problemDetail;
  }

  @ExceptionHandler({Exception.class, RuntimeException.class, Throwable.class})
  public ProblemDetail handleGlobalException(Exception ex) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Internal server error"
    );
    problemDetail.setTitle("Internal Server Error");
    problemDetail.setProperty("details", ex.getMessage());

    StackTraceElement[] stack = ex.getStackTrace();
    if (stack != null && stack.length > 0) {
      problemDetail.setProperty("trace", stack[0].toString());
    }

    return problemDetail;
  }
}
