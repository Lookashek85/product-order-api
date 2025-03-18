package dev.tutorial.productorderservice.adapters.http.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class UpdateProductRequest {
  private String productName;
  private BigDecimal price;
}
