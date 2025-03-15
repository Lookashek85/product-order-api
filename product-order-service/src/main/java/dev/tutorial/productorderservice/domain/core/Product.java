package dev.tutorial.productorderservice.domain.core;

import dev.tutorial.productorderservice.domain.core.valueobjects.Name;
import dev.tutorial.productorderservice.domain.core.valueobjects.Price;
import dev.tutorial.productorderservice.domain.core.valueobjects.ProductId;

public record Product(ProductId productId, Name productName, Price price) {}
