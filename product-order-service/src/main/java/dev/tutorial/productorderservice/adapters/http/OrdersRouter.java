package dev.tutorial.productorderservice.adapters.http;

import dev.tutorial.productorderservice.adapters.http.api.OrdersOpenApi;
import dev.tutorial.productorderservice.adapters.http.requests.CreateOrderRequest;
import dev.tutorial.productorderservice.adapters.http.responses.OrderResponse;
import dev.tutorial.productorderservice.adapters.http.responses.OrdersResponse;
import dev.tutorial.productorderservice.domain.commands.CreateOrderCommand;
import dev.tutorial.productorderservice.domain.core.valueobjects.Email;
import dev.tutorial.productorderservice.domain.core.valueobjects.OrderTimestamp;
import dev.tutorial.productorderservice.domain.core.valueobjects.ProductId;
import dev.tutorial.productorderservice.domain.services.OrderService;
import jakarta.validation.Valid;
import java.time.Instant;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/orders")
public class OrdersRouter implements OrdersOpenApi {

  private final Logger log = LoggerFactory.getLogger(OrdersRouter.class);

  private final OrderService orderService;

  public OrdersRouter(OrderService orderService) {
    this.orderService = orderService;
  }

  @PostMapping
  public ResponseEntity<OrderResponse> create(@RequestBody @Valid CreateOrderRequest request) {
    log.info(" POST /v1/orders request: {}", request);
    var order = OrderResponse.of(orderService.createOrder(toCommand(request)));
    return ResponseEntity.status(HttpStatus.CREATED).body(order);
  }

  @GetMapping
  public ResponseEntity<OrdersResponse> getOrdersWithinDates(
      @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
      @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to) {
    log.info(" GET /v1/orders request  within {} and to {}: ", from, to);
    var orders = orderService.getOrders(OrderTimestamp.of(from), OrderTimestamp.of(to));
    return ResponseEntity.ok(OrdersResponse.of(orders.stream().map(OrderResponse::of).toList()));
  }

  private CreateOrderCommand toCommand(CreateOrderRequest request) {
    return new CreateOrderCommand(
        Email.of(request.getBuyerEmail()),
        request.getProductIds().stream().map(ProductId::fromString).collect(Collectors.toList()));
  }
}
