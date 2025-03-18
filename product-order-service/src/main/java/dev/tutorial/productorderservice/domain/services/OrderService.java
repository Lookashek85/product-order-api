package dev.tutorial.productorderservice.domain.services;

import dev.tutorial.productorderservice.domain.commands.CreateOrderCommand;
import dev.tutorial.productorderservice.domain.core.Order;
import dev.tutorial.productorderservice.domain.core.valueobjects.OrderTimestamp;
import java.util.List;

public interface OrderService {
  Order createOrder(CreateOrderCommand createOrderCommand);

  List<Order> getOrders(OrderTimestamp from, OrderTimestamp to); // todo replace with Range object
}
