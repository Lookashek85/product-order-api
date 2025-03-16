package dev.tutorial.productorderservice.domain.services.repositories;

import dev.tutorial.productorderservice.domain.core.Order;
import java.time.Instant;
import java.util.List;

public interface OrderRepository {
  Order create(Order Order);

  List<Order> findWithTimeRange(Instant from, Instant to);

  void deleteAll();
}
