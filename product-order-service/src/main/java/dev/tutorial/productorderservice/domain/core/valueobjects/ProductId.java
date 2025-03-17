package dev.tutorial.productorderservice.domain.core.valueobjects;

import java.util.UUID;

public class ProductId extends BaseId<ProductId> {
  protected ProductId(UUID value) {
    super(value);
  }

  public static ProductId generate() {
    return new ProductId(UUID.randomUUID());
  }

  public static ProductId fromUUID(UUID uuid) {
    return new ProductId(uuid);
  }

  public static ProductId fromString(String productId) {
    return new ProductId(UUID.fromString(productId));
  }
}
