package dev.tutorial.productorderservice.utils;

import dev.tutorial.productorderservice.domain.core.valueobjects.OrderTimestamp;

public interface TimestampProvider {
  OrderTimestamp now();
}
