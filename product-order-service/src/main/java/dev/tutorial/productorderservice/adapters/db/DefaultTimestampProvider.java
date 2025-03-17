package dev.tutorial.productorderservice.adapters.db;

import dev.tutorial.productorderservice.domain.core.valueobjects.OrderTimestamp;
import dev.tutorial.productorderservice.utils.TimestampProvider;
import java.time.Instant;
import org.springframework.stereotype.Component;

@Component
public class DefaultTimestampProvider implements TimestampProvider {

  @Override
  public OrderTimestamp now() {
    return new OrderTimestamp(Instant.now());
  }
}
