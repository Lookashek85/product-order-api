package dev.tutorial.productorderservice.adapters.http;

import dev.tutorial.productorderservice.domain.services.OrderService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/orders")
public class OrdersRouter {

  private final OrderService orderService;

  public OrdersRouter(OrderService orderService) {
    this.orderService = orderService;
  }
}
