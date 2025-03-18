package dev.tutorial.productorderservice.adapters.http.api;

import dev.tutorial.productorderservice.adapters.http.requests.CreateOrderRequest;
import dev.tutorial.productorderservice.adapters.http.responses.OrderResponse;
import dev.tutorial.productorderservice.adapters.http.responses.OrdersResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.Instant;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Orders", description = "Product Orders API")
public interface OrdersOpenApi {

  @Operation(
      summary = "Get orders within date range",
      description = "Retrieves orders between specified timestamps")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Orders retrieved successfully"),
    @ApiResponse(responseCode = "400", description = "Invalid date parameters"),
    @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  ResponseEntity<OrdersResponse> getOrdersWithinDates(
      @Parameter(description = "Start date/time (ISO format)", example = "2025-03-01T00:00:00Z")
          @RequestParam("from")
          Instant from,
      @Parameter(description = "End date/time (ISO format)", example = "2025-03-31T23:59:59Z")
          @RequestParam("to")
          Instant to);

  @Operation(
      summary = "Create new order",
      description = "Creates a new order with specified details")
  @ApiResponses({
    @ApiResponse(responseCode = "201", description = "Order created successfully"),
    @ApiResponse(responseCode = "400", description = "Invalid order data"),
    @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  ResponseEntity<OrderResponse> create(
      @RequestBody(
              description = "Order creation data",
              required = true,
              content = @Content(schema = @Schema(implementation = CreateOrderRequest.class)))
          @Valid
          CreateOrderRequest request);
}
