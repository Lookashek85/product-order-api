package dev.tutorial.productorderservice.adapters.http.requests;

import java.math.BigDecimal;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateProductRequest {
  private Optional<String> productName;
  private Optional<BigDecimal> price;
}
