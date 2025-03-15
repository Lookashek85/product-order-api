package dev.tutorial.productorderservice.domain.core;

import dev.tutorial.productorderservice.domain.core.valueobjects.Email;
import dev.tutorial.productorderservice.domain.core.valueobjects.OrderId;
import dev.tutorial.productorderservice.domain.core.valueobjects.Price;
import dev.tutorial.productorderservice.domain.core.valueobjects.Timestamp;
import java.util.List;

public record Order(
    OrderId orderId,
    List<Product> products,
    Price totalPrice,
    Email buyerEmail,
    Timestamp orderTimestamp) {}
