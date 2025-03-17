package dev.tutorial.productorderservice.integration.db;

import dev.tutorial.productorderservice.BaseDbIntegrationTest;
import dev.tutorial.productorderservice.ProductOrderServiceApplication;
import dev.tutorial.productorderservice.TestTimestampProvider;
import dev.tutorial.productorderservice.domain.core.Order;
import dev.tutorial.productorderservice.domain.core.Product;
import dev.tutorial.productorderservice.domain.core.valueobjects.Email;
import dev.tutorial.productorderservice.domain.core.valueobjects.Name;
import dev.tutorial.productorderservice.domain.core.valueobjects.OrderTimestamp;
import dev.tutorial.productorderservice.domain.core.valueobjects.Price;
import dev.tutorial.productorderservice.domain.core.valueobjects.ProductId;
import dev.tutorial.productorderservice.domain.services.repositories.OrderRepository;
import dev.tutorial.productorderservice.domain.services.repositories.ProductRepository;
import dev.tutorial.productorderservice.utils.TimestampProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("test")
@SpringBootTest(classes = {ProductOrderServiceApplication.class, OrderRepositoryTest.TestConfig.class})
public class OrderRepositoryTest extends BaseDbIntegrationTest {

  private final Email buyerEmail = new Email("some@email.com");

  @Autowired private ProductRepository productRepository;
  @Autowired private OrderRepository orderRepository;

  @Autowired
  private TimestampProvider timestampProvider;

  @BeforeEach
  void setUp() {
    orderRepository.deleteAll();
    productRepository.deleteAll();
  }

  @Test
  void shouldCreateAndRetrieveOrder() {
    var product1 = new Product(ProductId.generate(), new Name("milk"), new Price(BigDecimal.TEN));
    var product2 =
        new Product(ProductId.generate(), new Name("chocolate"), new Price(BigDecimal.TEN));
    productRepository.save(product1);
    productRepository.save(product2);
    var products = List.of(product1, product2);
    var total = products.stream().map(Product::price).reduce(Price.ZERO, Price::add);

    var order = new Order(null, products, total, buyerEmail, new OrderTimestamp(Instant.now()));
    var orderSaved = orderRepository.create(order);
    assertThat(orderSaved).isNotNull();
    assertThat(orderSaved.buyerEmail()).isEqualTo(buyerEmail);

    assertThat(new HashSet<>(orderSaved.products())).isEqualTo(new HashSet<>(products));
  }

  @Test
  void shouldCreateAndRetrieveOrdersWithUpdatedProducts() {
    var product1 = new Product(ProductId.generate(), new Name("milk"), new Price(BigDecimal.TEN));
    productRepository.save(product1);
    var products = List.of(product1);
    var total = product1.price();

    var order = new Order(null, products, total, buyerEmail, new OrderTimestamp(Instant.now()));
    var orderSaved = orderRepository.create(order);

    var updatedName = new Name("chocolate");
    var updatedPrice = new Price(BigDecimal.TWO);
    var updatedProduct = new Product(product1.productId(), updatedName, updatedPrice);
    productRepository.save(updatedProduct);
    var updatedProductsList = List.of(updatedProduct);
    var nextOrder =
        new Order(null, updatedProductsList, total, buyerEmail, new OrderTimestamp(Instant.now()));
    var nextOrderSaved = orderRepository.create(nextOrder);

    assertThat(order).isNotNull();
    assertThat(orderSaved.products()).isEqualTo(products);

    assertThat(nextOrder).isNotNull();
    assertThat(nextOrderSaved.products()).isEqualTo(updatedProductsList);
  }

    @Test
    void shouldRetrieveOrdersByGivenTimeRange() {
      // GIVEN orders were created on different dates
      var daysAgo = 20;
      var twentyDaysAgo = Instant.now().minus(daysAgo, DAYS);
      ((TestTimestampProvider) timestampProvider).setFixedTimestamp(twentyDaysAgo);

      var milk = new Product(ProductId.generate(), new Name("milk"), new Price(BigDecimal.TEN));
      var chocolate = new Product(ProductId.generate(), new Name("chocolate"), new Price(BigDecimal.TWO));
      productRepository.save(milk);
      productRepository.save(chocolate);
      var productsEarlyOrder = List.of(milk);
      var earlyOrder = new Order(null, productsEarlyOrder, milk.price(), buyerEmail, new OrderTimestamp(Instant.now()));

      var orderSavedEarly = orderRepository.create(earlyOrder);

      ((TestTimestampProvider) timestampProvider).setFixedTimestamp(Instant.now());

      var laterOrder = new Order(null, productsEarlyOrder, milk.price(), buyerEmail, new OrderTimestamp(Instant.now()));

      var orderSavedLater = orderRepository.create(laterOrder);

      // Retrieve orders within the correct time range
      var earliestOrders = orderRepository.findWithTimeRange(twentyDaysAgo, twentyDaysAgo.minus(5, DAYS));
      assertThat(earliestOrders).isNotNull();
      assertThat(earliestOrders.size()).isEqualTo(1);
      assertThat(earliestOrders.getFirst().orderId()).isEqualTo(orderSavedEarly.orderId());
    }
  @TestConfiguration
  static class TestConfig {
    @Bean
    @Primary
    public TimestampProvider timestampProvider() {
      return new TestTimestampProvider();
    }
  }
}
