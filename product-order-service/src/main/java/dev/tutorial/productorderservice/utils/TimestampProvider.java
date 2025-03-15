package dev.tutorial.productorderservice.utils;

import dev.tutorial.productorderservice.domain.core.valueobjects.Timestamp;

public interface TimestampProvider {
  Timestamp now();
}
