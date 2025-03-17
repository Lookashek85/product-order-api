package dev.tutorial.productorderservice.domain.core.valueobjects;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Price {
  private final BigDecimal value;

  public Price(BigDecimal value) {
    if (value == null) {
      throw new NullPointerException("Price cannot be null");
    }
    if (value.compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException("Price cannot be negative");
    }
    this.value = value.setScale(2, RoundingMode.HALF_UP); // Normalize to 2 decimal places
  }

  public static Price of(BigDecimal value) {
    return new Price(value);
  }

  public BigDecimal getValue() {
    return value;
  }

  public Price add(Price other) {
    return new Price(value.add(other.value));
  }

  public static final Price ZERO = new Price(BigDecimal.ZERO);

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Price price = (Price) o;
    return Objects.equals(value, price.value);
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
