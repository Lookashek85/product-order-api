package dev.tutorial.productorderservice.domain.core.valueobjects;

import java.time.Instant;
import java.util.Objects;

public record Timestamp(Instant value) {

  public Timestamp {
    if (value == null) {
      throw new NullPointerException("Timestamp cannot be null");
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Timestamp timestamp = (Timestamp) o;
    return Objects.equals(value, timestamp.value);
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
