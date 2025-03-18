package dev.tutorial.productorderservice.adapters.http;

import dev.tutorial.productorderservice.domain.core.DomainError;
import dev.tutorial.productorderservice.domain.core.ProductNotFound;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(DomainError.class)
  public ResponseEntity<Map<String, String>> handleDomainErrors(DomainError error) {
    Map<String, String> errors = new HashMap<>();
    errors.put("message", error.getMessage());
    errors.put("object", error.getDomainObjectName());
    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ProductNotFound.class)
  public ResponseEntity<Map<String, String>> handleProductNotFound(DomainError error) {
    Map<String, String> errors = new HashMap<>();
    errors.put("message", error.getMessage());
    return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidationExceptions(
      MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult()
        .getFieldErrors()
        .forEach(
            error -> {
              errors.put(error.getField(), error.getDefaultMessage());
            });
    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({NumberFormatException.class, IllegalArgumentException.class})
  public ResponseEntity<Map<String, String>> handleBadArgumentsExceptions(Exception ex) {
    Map<String, String> error = new HashMap<>();
    error.put(
        "error",
        "Invalid input was given. Please check your request. Error message: " + ex.getMessage());
    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, String>> handleGeneralExceptions(Exception ex) {
    Map<String, String> error = new HashMap<>();
    error.put("error", "An unexpected error occurred.");
    return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
