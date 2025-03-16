package dev.tutorial.productorderservice.adapters.db;

import dev.tutorial.productorderservice.domain.core.valueobjects.OrderTimestamp;
import dev.tutorial.productorderservice.utils.TimestampProvider;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class DefaultTimestampProvider implements TimestampProvider {

    @Override
    public OrderTimestamp now() {
        return new OrderTimestamp(Instant.now());
    }
}
