package dev.tutorial.productorderservice.integration.db;

import dev.tutorial.productorderservice.BaseDbIntegrationTest;
import dev.tutorial.productorderservice.domain.core.Product;
import dev.tutorial.productorderservice.domain.core.valueobjects.Name;
import dev.tutorial.productorderservice.domain.core.valueobjects.Price;
import dev.tutorial.productorderservice.domain.core.valueobjects.ProductId;
import dev.tutorial.productorderservice.domain.services.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

//@ActiveProfiles("test")
public class ProductRepositoryTest extends BaseDbIntegrationTest {

  @Autowired private ProductRepository productRepository;

  @BeforeEach
  void setUp() {
    productRepository.deleteAll();
  }

  @Test
  void shouldSaveAndRetrieveProducts() {
    // Given
    var product1 = new Product(ProductId.generate(), new Name("milk"), new Price(BigDecimal.TEN));
    var product2 =
        new Product(ProductId.generate(), new Name("chocolate"), new Price(BigDecimal.TEN));
    // When
    var savedProduct1 = productRepository.save(product1);
    var savedProduct2 = productRepository.save(product2);
    // Then
    assertThat(savedProduct1).isNotNull();
    assertThat(savedProduct1.productId()).isNotNull();
    assertThat(savedProduct1.productName().value()).isEqualTo("milk");

    assertThat(savedProduct2).isNotNull();
    assertThat(savedProduct2.productId()).isNotNull();
    assertThat(savedProduct2.productName().value()).isEqualTo("chocolate");

    assertThat(productRepository.findAll().size()).isEqualTo(2);
  }

  @Test
  void shouldUpdateProduct() {
    // Given
    var productId = ProductId.generate();
    var newName = new Name("chocolate");
    var newPrice = new Price(new BigDecimal("15.00"));
    var current = new Product(productId, new Name("milk"), new Price(BigDecimal.TEN));
    productRepository.save(current);
    var updatedProduct = new Product(productId, newName, newPrice);
    // When
    productRepository.update(updatedProduct);

    var updateResult = productRepository.findById(productId).orElseThrow();

    // Then
    assertThat(updateResult).isNotNull();
    assertThat(updateResult.productId()).isEqualTo(productId);
    assertThat(updateResult.productName()).isEqualTo(newName);
    assertThat(updateResult.price()).isEqualTo(newPrice);
  }
}
