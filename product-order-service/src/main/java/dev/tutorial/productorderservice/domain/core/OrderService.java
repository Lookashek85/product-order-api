package dev.tutorial.productorderservice.domain.core;

import dev.tutorial.productorderservice.domain.core.valueobjects.Timestamp;
import java.util.List;

public interface OrderService {
  Order createOrder(User user, List<Product> products);

  List<Order> getOrders(Timestamp from, Timestamp to); // todo replace with Range object
}
