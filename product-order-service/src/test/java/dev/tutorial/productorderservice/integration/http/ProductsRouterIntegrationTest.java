package dev.tutorial.productorderservice.integration.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.tutorial.productorderservice.BaseDbIntegrationTest;
import dev.tutorial.productorderservice.ProductOrderServiceApplication;
import dev.tutorial.productorderservice.adapters.http.requests.UpdateProductRequest;
import dev.tutorial.productorderservice.domain.core.Product;
import dev.tutorial.productorderservice.domain.core.valueobjects.Name;
import dev.tutorial.productorderservice.domain.core.valueobjects.Price;
import dev.tutorial.productorderservice.domain.core.valueobjects.ProductId;
import dev.tutorial.productorderservice.domain.services.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = ProductOrderServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ProductsRouterIntegrationTest extends BaseDbIntegrationTest {

    private WebTestClient webTestClient;

    @LocalServerPort
    private int port;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/v1//products";
        productRepository.deleteAll();
        webTestClient = WebTestClient.bindToServer().baseUrl(baseUrl).build();
    }

    @Test
    void updatingProductWithValidInputsReturnsOK() throws Exception {
        // Arrange
        // 1. Create a product first
        var product = new Product(ProductId.generate(), Name.of("milk"), Price.of(BigDecimal.TEN));
        var savedProduct = productRepository.save(product);
        var productId = savedProduct.productId();

        UpdateProductRequest updateRequest = new UpdateProductRequest("bread",BigDecimal.ONE, productId.getValue().toString());
        String requestBody = objectMapper.writeValueAsString(updateRequest);

        // Act
        WebTestClient.ResponseSpec response = webTestClient.put()
                .uri(baseUrl + "/" + productId.getValue())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange();

        // Assert
        response.expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.productId").isEqualTo(productId.getValue().toString())
                .jsonPath("$.productName").isEqualTo("bread")
                .jsonPath("$.productPrice").isEqualTo(1.00);

        // Verify the product is actually updated in the database
        Product updatedProduct = productRepository.findById(productId).orElse(null);
        assertThat(updatedProduct).isNotNull();
        assertThat(updatedProduct.productName().value()).isEqualTo("bread");
        assertThat(updatedProduct.price().getValue()).isEqualTo(new BigDecimal("1.00"));
    }


    @Test
    void updateProductsWithWrongInputsReturnsBadRequestAndValidationMessages() throws Exception {
        var product = new Product(ProductId.generate(), Name.of("milk"), Price.of(new  BigDecimal("11.00")));
        var savedProduct = productRepository.save(product);
        var productId = savedProduct.productId();
        UpdateProductRequest updateRequest = new UpdateProductRequest("",new  BigDecimal("-11.00"), productId.getValue().toString());
        String requestBody = objectMapper.writeValueAsString(updateRequest);

        WebTestClient.ResponseSpec response = webTestClient.put()
                .uri(baseUrl + "/" + productId.getValue())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange();

        // Assert
        response.expectStatus().isBadRequest()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.productName").isEqualTo("Product name cannot be blank")
                .jsonPath("$.price").isEqualTo("Price must be a positive value");

    }
//
//    @Test
//    void updateProduct_ProductNotFound_ReturnsNotFound() throws Exception {
//        // Arrange
//        Long nonExistentProductId = 999999L;  // An ID that's highly unlikely to exist
//        ProductCommand updateRequest = new ProductCommand("Updated Product Name", new BigDecimal("39.99"));
//        String requestBody = objectMapper.writeValueAsString(updateRequest);
//
//        // Act
//        WebTestClient.ResponseSpec response = webTestClient.put()
//                .uri(baseUrl + "/" + nonExistentProductId)
//                .contentType(MediaType.APPLICATION_JSON)
//                .bodyValue(requestBody)
//                .exchange();
//
//        // Assert
//        response.expectStatus().isNotFound();
//    }
}
