package dev.tutorial.productorderservice.domain.core.valueobjects;

import java.util.Objects;
import java.util.UUID;

public abstract class BaseId<T extends BaseId<T>> {
  private final UUID value;

  protected BaseId(UUID value) {
    this.value = value;
  }

  public UUID getValue() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    BaseId<?> that = (BaseId<?>) o;
    return Objects.equals(value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "(value=[" + value + "])";
  }
}
