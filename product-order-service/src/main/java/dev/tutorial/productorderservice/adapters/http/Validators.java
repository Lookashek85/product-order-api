package dev.tutorial.productorderservice.adapters.http;

import dev.tutorial.productorderservice.adapters.http.requests.UpdateProductRequest;
import dev.tutorial.productorderservice.domain.commands.UpdateProductCommand;
import dev.tutorial.productorderservice.domain.core.valueobjects.Name;
import dev.tutorial.productorderservice.domain.core.valueobjects.Price;
import dev.tutorial.productorderservice.domain.core.valueobjects.ProductId;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;

@Component
public class Validators {

  protected UpdateProductCommand toValidatedUpdateProductCommand(
      UpdateProductRequest request, String productId) {
    if (request.getProductName() == null && request.getPrice() == null) {
      throw new IllegalArgumentException(
          "At least a product name or price is required when updating product");
    }
    validateProductName(request);
    else if (request.getProductName() == null
        && request.getPrice().compareTo(BigDecimal.ZERO) > 0) {
      throw new IllegalArgumentException("Price must be a positive value");
    } else if (request.getPrice() == null && request.getProductName().isEmpty()) {
      throw new IllegalArgumentException("No name given for updated product");
    }
    return new UpdateProductCommand(
        new Name(request.getProductName()),
        new Price(request.getPrice()),
        ProductId.fromString(productId));
  }

  private void validateProductName(UpdateProductRequest request) {
    if (request.getProductName() == null  || request.getProductName().isEmpty()) {
     if(request.getPrice() == null){
       throw new IllegalArgumentException("No name given for updated product");
     }
    }
  }
}
