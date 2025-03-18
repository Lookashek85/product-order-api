package dev.tutorial.productorderservice.domain.core;

public class DomainError extends RuntimeException {

  private final String message;
  private final String domainObject;

  public DomainError(String domainObject, String message) {
    super(String.format("%s: %s", domainObject, message));
    this.message = message;
    this.domainObject = domainObject;
  }

  @Override
  public String getMessage() {
    return message;
  }

  public String getDomainObjectName() {
    return domainObject;
  }
}
