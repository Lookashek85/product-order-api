package dev.tutorial.productorderservice.domain.services;

import dev.tutorial.productorderservice.domain.core.Order;
import dev.tutorial.productorderservice.domain.core.Product;
import dev.tutorial.productorderservice.domain.core.User;
import dev.tutorial.productorderservice.domain.core.valueobjects.OrderTimestamp;
import java.util.List;

public interface OrderService {
  Order createOrder(User user, List<Product> products);

  List<Order> getOrders(OrderTimestamp from, OrderTimestamp to); // todo replace with Range object
}
