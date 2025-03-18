package dev.tutorial.productorderservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.tutorial.productorderservice.adapters.http.requests.CreateOrderRequest;
import dev.tutorial.productorderservice.adapters.http.responses.OrderResponse;
import dev.tutorial.productorderservice.adapters.http.responses.ProductResponse;
import dev.tutorial.productorderservice.domain.core.Product;
import dev.tutorial.productorderservice.domain.core.valueobjects.Name;
import dev.tutorial.productorderservice.domain.core.valueobjects.Price;
import dev.tutorial.productorderservice.domain.core.valueobjects.ProductId;
import dev.tutorial.productorderservice.domain.services.ProductService;
import dev.tutorial.productorderservice.domain.services.repositories.OrderRepository;
import dev.tutorial.productorderservice.domain.services.repositories.ProductRepository;
import dev.tutorial.productorderservice.utils.TimestampProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.util.Predicates.isTrue;

//@SpringBootTest(
//    classes = ProductOrderServiceApplication.class,
//    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@ActiveProfiles("test")
public class FullIntegrationTest extends BaseDbIntegrationTest {

  private WebTestClient webTestClient;

  @LocalServerPort private int port;

  @Autowired private ObjectMapper objectMapper;

  @Autowired private ProductService productService;
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
    // GIVEN
    var buyerEmail = "buyer@email.com";
    var productMilk = new Product(ProductId.generate(), Name.of("milk"), Price.of(BigDecimal.TEN));
    var savedProductMilk = productRepository.save(productMilk);
    var productMilkId = savedProductMilk.productId();

    var productBread =
        new Product(ProductId.generate(), Name.of("bread"), Price.of(new BigDecimal("12.5")));
    var savedProductBread = productRepository.save(productBread);
    var productBreadId = savedProductBread.productId();

    var createOrderRequest =
        new CreateOrderRequest(
            buyerEmail,
            List.of(productMilkId.getValue().toString(), productBreadId.getValue().toString()));
    var requestBody = objectMapper.writeValueAsString(createOrderRequest);

    WebTestClient.ResponseSpec response =
        webTestClient
            .post()
            .uri(baseUrl)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(requestBody)
            .exchange();

    response.expectStatus().isCreated();

    var orderOneCreatedResponse =
        response.expectBody(OrderResponse.class).returnResult().getResponseBody();

    assertThat(orderOneCreatedResponse).isNotNull();
    var expectedProductIds = Set.of(productMilkId.getValue().toString(), productBreadId.getValue().toString());
    var actualProductIds = orderOneCreatedResponse.getProducts().stream().map(ProductResponse::getProductId).collect(Collectors.toSet());

    assertThat(orderOneCreatedResponse).isNotNull();
    assertThat(orderOneCreatedResponse.getBuyerEmail()).isEqualTo(buyerEmail);
    assertThat(expectedProductIds.containsAll(actualProductIds)).isTrue();
  }
}
