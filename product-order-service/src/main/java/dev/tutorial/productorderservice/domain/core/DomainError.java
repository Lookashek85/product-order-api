package dev.tutorial.productorderservice.domain.core;

public class DomainError extends RuntimeException {

  private final String className;

  public DomainError(String className, String message) {
    super(String.format("%s: %s", className, message));
    this.className = className;
  }

  public String getClassName() {
    return className;
  }
}
