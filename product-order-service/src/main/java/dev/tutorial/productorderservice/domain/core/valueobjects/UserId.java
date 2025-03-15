package dev.tutorial.productorderservice.domain.core.valueobjects;

import java.util.UUID;

public class UserId extends BaseId<UserId> {
  protected UserId(UUID value) {
    super(value);
  }

  public static UserId generate() {
    return new UserId(UUID.randomUUID());
  }
}
