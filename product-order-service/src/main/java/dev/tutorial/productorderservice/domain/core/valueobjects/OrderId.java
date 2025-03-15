package dev.tutorial.productorderservice.domain.core.valueobjects;

import java.util.UUID;

public class OrderId extends BaseId<OrderId> {
  protected OrderId(UUID value) {
    super(value);
  }

  public static OrderId generate() {
    return new OrderId(UUID.randomUUID());
  }
}
