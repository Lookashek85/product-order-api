package dev.tutorial.productorderservice.domain.core.valueobjects;

import java.time.Instant;
import java.util.Objects;

public record OrderTimestamp(Instant value) {

  public OrderTimestamp {
    if (value == null) {
      throw new NullPointerException("Timestamp cannot be null");
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    OrderTimestamp orderTimestamp = (OrderTimestamp) o;
    return Objects.equals(value, orderTimestamp.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }

  @Override
  public String toString() {
    return value.toString();
  }
}
