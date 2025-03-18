package dev.tutorial.productorderservice.adapters.http.responses;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrdersResponse {
  private List<OrderResponse> orders;

  public static OrdersResponse of(List<OrderResponse> orders) {
    return new OrdersResponse(orders);
  }
}
