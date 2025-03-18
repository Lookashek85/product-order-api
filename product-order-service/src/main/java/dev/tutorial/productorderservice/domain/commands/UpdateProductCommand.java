package dev.tutorial.productorderservice.domain.commands;

import dev.tutorial.productorderservice.domain.core.valueobjects.Name;
import dev.tutorial.productorderservice.domain.core.valueobjects.Price;
import dev.tutorial.productorderservice.domain.core.valueobjects.ProductId;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateProductCommand {
  private final ProductId productId;
  private final Name productName;
  private final Price price;
}
