package dev.tutorial.productorderservice.integration.http;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.tutorial.productorderservice.BaseDbIntegrationTest;
import dev.tutorial.productorderservice.ProductOrderServiceApplication;
import dev.tutorial.productorderservice.adapters.http.requests.UpdateProductRequest;
import dev.tutorial.productorderservice.domain.core.Product;
import dev.tutorial.productorderservice.domain.core.valueobjects.Name;
import dev.tutorial.productorderservice.domain.core.valueobjects.Price;
import dev.tutorial.productorderservice.domain.core.valueobjects.ProductId;
import dev.tutorial.productorderservice.domain.services.repositories.ProductRepository;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(
    classes = ProductOrderServiceApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ProductsRouterIntegrationTest extends BaseDbIntegrationTest {

  private WebTestClient webTestClient;

  @LocalServerPort private int port;

  @Autowired private ObjectMapper objectMapper;

  @Autowired private ProductRepository productRepository;

  private String baseUrl;

  @BeforeEach
  void setUp() {
    baseUrl = "http://localhost:" + port + "/v1/products";
    productRepository.deleteAll();
    webTestClient = WebTestClient.bindToServer().baseUrl(baseUrl).build();
  }

  //TODO Create with wrong inputs tests

  @Test
  void updatingProductWithValidInputsReturnsOK() throws Exception {
    // Given
    var product = new Product(ProductId.generate(), Name.of("milk"), Price.of(BigDecimal.TEN));
    var savedProduct = productRepository.save(product);
    var productId = savedProduct.productId();

    var updateRequest = new UpdateProductRequest("bread", BigDecimal.ONE);
    var requestBody = objectMapper.writeValueAsString(updateRequest);

    // When
    WebTestClient.ResponseSpec response =
        webTestClient
            .put()
            .uri(baseUrl + "/" + productId.getValue())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(requestBody)
            .exchange();

    // Then
    response
        .expectStatus()
        .isOk()
        .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.productId")
        .isEqualTo(productId.getValue().toString())
        .jsonPath("$.productName")
        .isEqualTo("bread")
        .jsonPath("$.productPrice")
        .isEqualTo(1.00);

    Product updatedProduct = productRepository.findById(productId).orElse(null);
    assertThat(updatedProduct).isNotNull();
    assertThat(updatedProduct.productName().value()).isEqualTo("bread");
    assertThat(updatedProduct.price().getValue()).isEqualTo(new BigDecimal("1.00"));
  }

  @Test
  void updateProductsWithWrongInputsReturnsBadRequestAndValidationMessages() throws Exception {
    // Given
    var product =
        new Product(ProductId.generate(), Name.of("milk"), Price.of(new BigDecimal("11.00")));
    var savedProduct = productRepository.save(product);
    var productId = savedProduct.productId();
    UpdateProductRequest updateRequest = new UpdateProductRequest("", new BigDecimal("-11.00"));
    String requestBody = objectMapper.writeValueAsString(updateRequest);

    // When
    WebTestClient.ResponseSpec response =
        webTestClient
            .put()
            .uri(baseUrl + "/" + productId.getValue())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(requestBody)
            .exchange();

    // Then
    response
        .expectStatus()
        .isBadRequest()
        .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.productName")
        .isEqualTo("Product name cannot be blank")
        .jsonPath("$.price")
        .isEqualTo("Price must be a positive value");
  }

  @Test
  void updateProduct_ProductNotFound_ReturnsNotFound() throws Exception {
    // Given
    var productId = ProductId.generate();
    UpdateProductRequest updateRequest =
        new UpdateProductRequest("Updated Product Name", new BigDecimal("39.99"));
    String requestBody = objectMapper.writeValueAsString(updateRequest);
    var uri = baseUrl + "/" + productId.getValue().toString();
    // When
    WebTestClient.ResponseSpec response =
        webTestClient
            .put()
            .uri(uri)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(requestBody)
            .exchange();

    // Then
    response.expectStatus().isNotFound();
  }
}
