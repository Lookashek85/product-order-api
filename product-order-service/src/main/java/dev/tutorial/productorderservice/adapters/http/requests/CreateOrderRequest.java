package dev.tutorial.productorderservice.adapters.http.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {
  @NotBlank(message = "Buyer email cannot be blank")
  private String buyerEmail;

  @NotEmpty(message = "At least one product id is required for order")
  private List<String> productIds;
}
