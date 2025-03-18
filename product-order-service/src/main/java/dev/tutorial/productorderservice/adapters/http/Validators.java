package dev.tutorial.productorderservice.adapters.http;

import dev.tutorial.productorderservice.adapters.http.requests.UpdateProductRequest;
import dev.tutorial.productorderservice.domain.commands.UpdateProductCommand;
import dev.tutorial.productorderservice.domain.core.valueobjects.Name;
import dev.tutorial.productorderservice.domain.core.valueobjects.Price;
import dev.tutorial.productorderservice.domain.core.valueobjects.ProductId;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class Validators {

  public UpdateProductCommand toValidatedUpdateProductCommand(
      UpdateProductRequest request, String productId) {

    List<String> productErrors = new ArrayList<>();
    List<String> priceErrors = new ArrayList<>();
    var builder = UpdateProductCommand.builder().productId(ProductId.fromString(productId));
    validateProductName(request, productErrors);
    validateProductPrice(request, priceErrors);

    if (productErrors.isEmpty() && priceErrors.isEmpty()) {
      builder.productName(Name.of(request.getProductName())).price(Price.of(request.getPrice()));
    } else if (request.getProductName() == null && priceErrors.isEmpty()) {
      builder.price(Price.of(request.getPrice()));
    } else if (request.getPrice() == null && productErrors.isEmpty()) {
      builder.productName(Name.of(request.getProductName()));
    } else {
      // validation error
      var errors = new ArrayList<String>();
      errors.addAll(productErrors);
      errors.addAll(priceErrors);
      throw new IllegalArgumentException(String.join("\n", errors));
    }
    return builder.build();
  }

  private void validateProductPrice(UpdateProductRequest request, List<String> errors) {
    var isInvalidPrice =
        request.getPrice() == null || request.getPrice().compareTo(BigDecimal.ZERO) < 0;
    if (isInvalidPrice) {
      errors.add("Price must be greater than zero");
    }
  }

  private void validateProductName(UpdateProductRequest request, List<String> errors) {
    var isInvalidName = request.getProductName() == null || request.getProductName().isBlank();
    if (isInvalidName) {
      errors.add("Product name is required for updating product");
    }
  }
}
