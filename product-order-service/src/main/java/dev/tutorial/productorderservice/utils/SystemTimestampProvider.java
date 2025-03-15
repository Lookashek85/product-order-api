package dev.tutorial.productorderservice.utils;

import dev.tutorial.productorderservice.domain.core.valueobjects.Timestamp;
import java.time.Instant;

public class SystemTimestampProvider implements TimestampProvider {
  @Override
  public Timestamp now() {
    return new Timestamp(Instant.now());
  }
}
