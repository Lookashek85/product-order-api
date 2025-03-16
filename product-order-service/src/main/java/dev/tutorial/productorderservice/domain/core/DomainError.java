package dev.tutorial.productorderservice.domain.core;

public class DomainError extends RuntimeException {

  public DomainError(String className, String message) {
    super(String.format("%s: %s", className, message));
  }
}
