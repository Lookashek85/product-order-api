package dev.tutorial.productorderservice.domain.core.valueobjects;

import java.util.Objects;

public record Name(String value) {
  public Name(String value) {
    if (value == null || value.isEmpty()) {
      throw new IllegalArgumentException("Name cannot be null or empty");
    }
    this.value = value.trim();
  }

  public static Name of(String value) {
    return new Name(value);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Name that = (Name) o;
    return Objects.equals(value, that.value);
  }

  @Override
  public String toString() {
    return value;
  }
}
