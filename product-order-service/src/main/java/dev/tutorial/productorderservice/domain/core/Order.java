package dev.tutorial.productorderservice.domain.core;

import dev.tutorial.productorderservice.domain.core.valueobjects.OrderId;
import dev.tutorial.productorderservice.domain.core.valueobjects.Price;
import java.util.List;

public record Order(OrderId orderId, List<Product> products, Price orderPrice) {
  public Price calculateTotalPrice() {
    return products.stream().map(Product::price).reduce(Price.ZERO, Price::add);
  }
}
