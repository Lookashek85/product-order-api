package dev.tutorial.productorderservice.adapters.http.requests;

import java.math.BigDecimal;

public class UpdateProductRequest extends ProductRequest {
  private final String productId;

  public UpdateProductRequest(String productName, BigDecimal price, String productId) {
    super(productName, price);
    this.productId = productId;
  }

  public String getProductId() {
    return this.productId;
  }
}
