package dev.tutorial.productorderservice.adapters.http.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {
  @NotBlank(message = "Buyer email cannot be blank")
  private String buyerEmail;

  @NotEmpty(message = "At least one product id is required for order")
  private List<String> productIds;
}
