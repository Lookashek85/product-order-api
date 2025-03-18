package dev.tutorial.productorderservice.adapters.http.responses;

import dev.tutorial.productorderservice.domain.core.Product;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ProductResponse {
  private String productId;
  private String productName;
  private BigDecimal productPrice;

  public static ProductResponse of(Product product) {
    return new ProductResponse(
        product.productId().getValue().toString(),
        product.productName().value(),
        product.price().getValue());
  }
}
