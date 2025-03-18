package dev.tutorial.productorderservice.domain.commands;

import dev.tutorial.productorderservice.domain.core.valueobjects.Name;
import dev.tutorial.productorderservice.domain.core.valueobjects.Price;

public record CreateProductCommand(Name productName, Price price) {
}
