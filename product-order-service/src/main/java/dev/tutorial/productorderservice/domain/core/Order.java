package dev.tutorial.productorderservice.domain.core;

import dev.tutorial.productorderservice.domain.core.valueobjects.Email;
import dev.tutorial.productorderservice.domain.core.valueobjects.OrderId;
import dev.tutorial.productorderservice.domain.core.valueobjects.OrderTimestamp;
import dev.tutorial.productorderservice.domain.core.valueobjects.Price;
import java.util.List;

public record Order(
    OrderId orderId,
    List<Product> products,
    Price totalPrice,
    Email buyerEmail,
    OrderTimestamp orderTimestamp) {}
