package dev.tutorial.productorderservice.domain.commands;

import dev.tutorial.productorderservice.domain.core.valueobjects.Name;
import dev.tutorial.productorderservice.domain.core.valueobjects.Price;
import dev.tutorial.productorderservice.domain.core.valueobjects.ProductId;

public class UpdateProductCommand extends CreateProductCommand {
  private final ProductId productId;

  public UpdateProductCommand(Name productName, Price price, ProductId productId) {
    super(productName, price);
    this.productId = productId;
  }

  public ProductId getProductId() {
    return this.productId;
  }
}
