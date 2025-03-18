package dev.tutorial.productorderservice.domain.commands;

import dev.tutorial.productorderservice.domain.core.valueobjects.Email;
import dev.tutorial.productorderservice.domain.core.valueobjects.ProductId;
import java.util.List;

public record CreateOrderCommand(Email buyerEmail, List<ProductId> productIds) {}
