package dev.tutorial.productorderservice.adapters.http.requests;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateProductRequest {
  private String productName;
  private BigDecimal price;
}
