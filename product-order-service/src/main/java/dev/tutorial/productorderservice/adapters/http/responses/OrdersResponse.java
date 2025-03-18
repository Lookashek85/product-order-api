package dev.tutorial.productorderservice.adapters.http.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class OrdersResponse {
  private List<OrderResponse> orders;

  public static OrdersResponse of(List<OrderResponse> orders) {
    return new OrdersResponse(orders);
  }
}
