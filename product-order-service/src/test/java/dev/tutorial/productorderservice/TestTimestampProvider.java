package dev.tutorial.productorderservice;

import dev.tutorial.productorderservice.domain.core.valueobjects.OrderTimestamp;
import dev.tutorial.productorderservice.utils.TimestampProvider;
import java.time.Instant;

public class TestTimestampProvider implements TimestampProvider {
  private Instant fixedInstant;

  public void setFixedTimestamp(Instant instant) {
    this.fixedInstant = instant;
  }

  @Override
  public OrderTimestamp now() {
    return fixedInstant != null
        ? new OrderTimestamp(fixedInstant)
        : new OrderTimestamp(Instant.now());
  }
}
