package dev.tutorial.productorderservice.utils;

import dev.tutorial.productorderservice.domain.core.valueobjects.OrderTimestamp;
import java.time.Instant;

public class SystemTimestampProvider implements TimestampProvider {
  @Override
  public OrderTimestamp now() {
    return new OrderTimestamp(Instant.now());
  }
}
