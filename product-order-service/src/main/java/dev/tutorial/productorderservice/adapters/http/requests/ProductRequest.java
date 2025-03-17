package dev.tutorial.productorderservice.adapters.http.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductRequest {
  @NotBlank(message = "Product name cannot be blank")
  private String productName;

  @NotNull(message = "Price cannot be null")
  @Positive(message = "Price must be a positive value")
  private BigDecimal price;
}
