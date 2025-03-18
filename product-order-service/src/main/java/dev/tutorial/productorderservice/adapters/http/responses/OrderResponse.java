package dev.tutorial.productorderservice.adapters.http.responses;

import dev.tutorial.productorderservice.domain.core.Order;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderResponse {
  private UUID orderId;
  private String buyerEmail;
  private Instant orderDate;
  private List<ProductResponse> products;
  private BigDecimal total;

  public static OrderResponse of(Order order) {
    return new OrderResponse(
        order.orderId().getValue(),
        order.buyerEmail().value(),
        order.orderTimestamp().value(),
        order.products().stream().map(ProductResponse::of).collect(Collectors.toList()),
        order.totalPrice().getValue());
  }
}
