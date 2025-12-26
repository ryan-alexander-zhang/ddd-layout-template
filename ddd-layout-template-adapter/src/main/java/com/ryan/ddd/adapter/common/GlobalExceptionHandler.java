package com.ryan.ddd.adapter.common;

import com.ryan.ddd.app.common.exception.AppException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
@Slf4j
@Order(Ordered.LOWEST_PRECEDENCE)
public class GlobalExceptionHandler {

  /**
   * Handle JSR 303 Exception
   *
   * @param ex MethodArgumentNotValidException
   * @return 400 with response
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Response> handleValidationExceptions(MethodArgumentNotValidException ex) {
    List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
    String errorMessage = fieldErrors.stream()
        .map(error -> error.getField() + ": " + error.getDefaultMessage())
        .collect(Collectors.joining("; "));
    log.warn("Validation failed: {}", errorMessage);
    return new ResponseEntity<>(
        Response.buildFailure(HttpStatus.BAD_REQUEST.getReasonPhrase(), errorMessage),
        HttpStatus.BAD_REQUEST);
  }

  /**
   * Handle application exceptions (translated from domain/infra inside app layer).
   */
  @ExceptionHandler(AppException.class)
  public ResponseEntity<Response> handleAppException(AppException ex) {
    int statusCode = ex.getErrorCode().httpStatus();
    HttpStatus status = HttpStatus.resolve(statusCode);
    if (status == null) {
      status = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    // Message is assumed to be client-safe at this boundary.
    log.warn("AppException: code={}, status={}, message={}", ex.getErrorCode().code(), statusCode,
        ex.getMessage(), ex);

    return new ResponseEntity<>(
        Response.buildFailure(ex.getErrorCode().code(), ex.getMessage()),
        status);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Response> handleIllegalArgumentException(IllegalArgumentException ex) {
    log.warn("IllegalArgumentException caught: ", ex);
    return new ResponseEntity<>(
        Response.buildFailure(HttpStatus.BAD_REQUEST.getReasonPhrase(), ex.getMessage()),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<Response> handleMethodArgumentTypeMismatchException(
      MethodArgumentTypeMismatchException ex) {
    String errorMessage = String.format(
        "Parameter '%s' with value '%s' could not be converted to type '%s'",
        ex.getName(), ex.getValue(),
        ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown");
    log.warn("MethodArgumentTypeMismatchException: {}", errorMessage, ex);
    return new ResponseEntity<>(
        Response.buildFailure(HttpStatus.BAD_REQUEST.getReasonPhrase(), errorMessage),
        HttpStatus.BAD_REQUEST);
  }

  /**
   * Handle HttpMessageNotReadableException This exception is thrown when the request body cannot be
   * read or parsed, for example, if the JSON format is invalid. This is typically caused by
   * malformed JSON in the request body.
   *
   * @param ex HttpMessageNotReadableException
   * @return 400 with response
   */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<Response> handleHttpMessageNotReadableException(
      HttpMessageNotReadableException ex) {
    log.warn("HttpMessageNotReadableException: {}", ex.getMessage());
    return new ResponseEntity<>(
        Response.buildFailure(HttpStatus.BAD_REQUEST.getReasonPhrase(),
            "Invalid JSON format. Please check your request params."), HttpStatus.BAD_REQUEST);
  }

  /**
   * Handle NoResourceFoundException This exception is thrown when a requested resource is not
   * found.
   *
   * @param ex NoResourceFoundException
   * @return 404 with response
   */
  @ExceptionHandler(NoResourceFoundException.class)
  public ResponseEntity<Response> handleNoResourceFoundException(NoResourceFoundException ex) {
    log.warn("No resource found: {}", ex.getMessage());
    return new ResponseEntity<>(Response.buildFailure(HttpStatus.NOT_FOUND.getReasonPhrase(),
        ex.getMessage()), HttpStatus.NOT_FOUND);
  }

  /**
   * Handle all other exceptions
   *
   * @param ex Throwable
   * @return 500 with response
   */
  @ExceptionHandler(Throwable.class)
  public ResponseEntity<Response> handleAllExceptions(Throwable ex) {
    log.error("An unexpected error occurred: ", ex);
    return new ResponseEntity<>(
        Response.buildFailure(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
            HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()), HttpStatus.INTERNAL_SERVER_ERROR);
  }
}