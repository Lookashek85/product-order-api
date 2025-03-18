package dev.tutorial.productorderservice.domain.core;

public class ProductNotFound extends DomainError {
  public ProductNotFound(String message) {
    super(ProductNotFound.class.getName(), message);
  }
}
