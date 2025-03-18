package dev.tutorial.productorderservice;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.MILLIS;
import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.tutorial.productorderservice.adapters.http.requests.CreateOrderRequest;
import dev.tutorial.productorderservice.adapters.http.responses.OrderResponse;
import dev.tutorial.productorderservice.adapters.http.responses.OrdersResponse;
import dev.tutorial.productorderservice.adapters.http.responses.ProductResponse;
import dev.tutorial.productorderservice.domain.core.Product;
import dev.tutorial.productorderservice.domain.core.valueobjects.Name;
import dev.tutorial.productorderservice.domain.core.valueobjects.Price;
import dev.tutorial.productorderservice.domain.core.valueobjects.ProductId;
import dev.tutorial.productorderservice.domain.services.repositories.OrderRepository;
import dev.tutorial.productorderservice.domain.services.repositories.ProductRepository;
import dev.tutorial.productorderservice.utils.TimestampProvider;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

public class FullIntegrationTest extends BaseDbIntegrationTest {

  private WebTestClient webTestClient;

  @LocalServerPort private int port;

  @Autowired private ObjectMapper objectMapper;

  @Autowired private ProductRepository productRepository;
  @Autowired private OrderRepository orderRepository;

  @Autowired private TimestampProvider timestampProvider;

  private String baseUrl;

  @BeforeEach
  void setUp() {
    baseUrl = "http://localhost:" + port + "/v1/orders";
    productRepository.deleteAll();
    orderRepository.deleteAll();
    webTestClient = WebTestClient.bindToServer().baseUrl(baseUrl).build();
  }

  @Test
  void addProducts_update_products_create_order_different_dates() throws Exception {
    // GIVEN time in the past
    var twentyDaysAgoVal = 20;
    var twentyDaysAgo = Instant.now().minus(twentyDaysAgoVal, DAYS).truncatedTo(MILLIS);
    ((TestTimestampProvider) timestampProvider).setFixedTimestamp(twentyDaysAgo);

    // GIVEN products are available for order
    var buyerEmail = "buyer@email.com";
    var productMilk = new Product(ProductId.generate(), Name.of("milk"), Price.of(BigDecimal.TEN));
    var savedProductMilk = productRepository.save(productMilk);
    var productMilkId = savedProductMilk.productId();

    var productBread =
        new Product(ProductId.generate(), Name.of("bread"), Price.of(new BigDecimal("12.5")));
    var savedProductBread = productRepository.save(productBread);
    var productBreadId = savedProductBread.productId();

    var expectedFirstOrderPriceValue = new BigDecimal("22.50");

    var createOrderRequest =
        new CreateOrderRequest(
            buyerEmail,
            List.of(productMilkId.getValue().toString(), productBreadId.getValue().toString()));
    var requestBody = objectMapper.writeValueAsString(createOrderRequest);

    // When creating first order
    var response =
        webTestClient
            .post()
            .uri(baseUrl)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(requestBody)
            .exchange()
            .expectStatus()
            .isCreated();

    var orderOneCreatedResponse =
        response.expectBody(OrderResponse.class).returnResult().getResponseBody();

    // Then available products are correctly added to the order
    assertThat(orderOneCreatedResponse).isNotNull();
    var expectedProductIds =
        Set.of(productMilkId.getValue().toString(), productBreadId.getValue().toString());
    var actualProductIds =
        orderOneCreatedResponse.getProducts().stream()
            .map(ProductResponse::getProductId)
            .collect(Collectors.toSet());

    assertThat(orderOneCreatedResponse).isNotNull();
    assertThat(orderOneCreatedResponse.getBuyerEmail()).isEqualTo(buyerEmail);
    assertThat(orderOneCreatedResponse.getTotal()).isEqualTo(expectedFirstOrderPriceValue);
    assertThat(expectedProductIds.containsAll(actualProductIds)).isTrue();

    // Given one product is updated by price and other not
    ((TestTimestampProvider) timestampProvider).setFixedTimestamp(Instant.now());
    var updatedProductMilk =
        new Product(
            savedProductMilk.productId(),
            Name.of("chocolate milk"),
            Price.of(new BigDecimal("20.00")));
    productRepository.update(updatedProductMilk);

    var expectedSecondOrderPriceValue = new BigDecimal("32.50");

    var createNextOrderRequest =
        new CreateOrderRequest(
            buyerEmail,
            List.of(productMilkId.getValue().toString(), productBreadId.getValue().toString()));
    var nextOrderRequestBody = objectMapper.writeValueAsString(createNextOrderRequest);

    // When creating second order
    var responseSecondOrder =
        webTestClient
            .post()
            .uri(baseUrl)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(nextOrderRequestBody)
            .exchange()
            .expectStatus()
            .isCreated();

    var orderTwoCreatedResponse =
        responseSecondOrder.expectBody(OrderResponse.class).returnResult().getResponseBody();

    assertThat(orderTwoCreatedResponse).isNotNull();

    var expectedOrderTwoProductIds =
        Set.of(productMilkId.getValue().toString(), productBreadId.getValue().toString());
    var actualOrderTwoProductIds =
        orderTwoCreatedResponse.getProducts().stream()
            .map(ProductResponse::getProductId)
            .collect(Collectors.toSet());

    var chocolateMilk =
        orderTwoCreatedResponse.getProducts().stream()
            .filter(p -> p.getProductId().equals(productMilkId.getValue().toString()))
            .findFirst()
            .get();
    assertThat(orderTwoCreatedResponse).isNotNull();
    assertThat(chocolateMilk.getProductName()).isEqualTo("chocolate milk");
    assertThat(orderTwoCreatedResponse.getBuyerEmail()).isEqualTo(buyerEmail);
    assertThat(orderTwoCreatedResponse.getTotal()).isEqualTo(expectedSecondOrderPriceValue);
    assertThat(expectedOrderTwoProductIds.containsAll(actualOrderTwoProductIds)).isTrue();

    var from20daysAgo = twentyDaysAgo.minus(1, DAYS).toString();
    var to10daysAgo = twentyDaysAgo.plus(10, DAYS).toString();

    var uri = baseUrl + "?from=" + from20daysAgo + "&to=" + to10daysAgo;

    var ret = webTestClient.get().uri(uri).accept(MediaType.APPLICATION_JSON).exchange();

    var earliestOrderResponse =
        ret.expectStatus().isOk().expectBody(OrdersResponse.class).returnResult().getResponseBody();

    var expectedOrderFound = orderOneCreatedResponse.getOrderId();

    assertThat(earliestOrderResponse).isNotNull();
    assertThat(
            earliestOrderResponse.getOrders().stream()
                .anyMatch(p -> p.getOrderId().equals(expectedOrderFound)))
        .isTrue();

    var fromLatest = Instant.now().minus(1, DAYS).toString();
    var toLatest = Instant.now().toString();

    var uriLatestOrders = baseUrl + "?from=" + fromLatest + "&to=" + toLatest;
    var retLatestOrders =
        webTestClient.get().uri(uriLatestOrders).accept(MediaType.APPLICATION_JSON).exchange();
    var latestOrderResponse =
        retLatestOrders
            .expectStatus()
            .isOk()
            .expectBody(OrdersResponse.class)
            .returnResult()
            .getResponseBody();

    var latestExpectedOrderFoundId = orderTwoCreatedResponse.getOrderId();
    assertThat(latestOrderResponse).isNotNull();
    var foundId = latestOrderResponse.getOrders().stream().findFirst().get().getOrderId();
    assertThat(foundId.equals(latestExpectedOrderFoundId)).isTrue();

    var uriAllOrders = baseUrl + "?from=" + from20daysAgo + "&to=" + toLatest;
    var allOrdersResponse =
            webTestClient.get().uri(uriAllOrders).accept(MediaType.APPLICATION_JSON).exchange().expectStatus()
            .isOk()
            .expectBody(OrdersResponse.class)
            .returnResult()
            .getResponseBody();

    assertThat(allOrdersResponse).isNotNull();
    assertThat(allOrdersResponse.getOrders()).size().isEqualTo(2);

  }
}
