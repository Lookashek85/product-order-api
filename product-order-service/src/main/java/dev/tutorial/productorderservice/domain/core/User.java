package dev.tutorial.productorderservice.domain.core;

import dev.tutorial.productorderservice.domain.core.valueobjects.Email;
import dev.tutorial.productorderservice.domain.core.valueobjects.Name;
import dev.tutorial.productorderservice.domain.core.valueobjects.UserId;

public record User(UserId userId, Name userName, Email email) {}
