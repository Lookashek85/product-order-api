package dev.tutorial.productorderservice.integration.db;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.MILLIS;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import dev.tutorial.productorderservice.BaseDbIntegrationTest;
import dev.tutorial.productorderservice.ProductOrderServiceApplication;
import dev.tutorial.productorderservice.TestTimestampProvider;
import dev.tutorial.productorderservice.domain.core.Order;
import dev.tutorial.productorderservice.domain.core.Product;
import dev.tutorial.productorderservice.domain.core.valueobjects.Email;
import dev.tutorial.productorderservice.domain.core.valueobjects.Name;
import dev.tutorial.productorderservice.domain.core.valueobjects.OrderId;
import dev.tutorial.productorderservice.domain.core.valueobjects.OrderTimestamp;
import dev.tutorial.productorderservice.domain.core.valueobjects.Price;
import dev.tutorial.productorderservice.domain.core.valueobjects.ProductId;
import dev.tutorial.productorderservice.domain.services.repositories.OrderRepository;
import dev.tutorial.productorderservice.domain.services.repositories.ProductRepository;
import dev.tutorial.productorderservice.utils.TimestampProvider;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(
    classes = {ProductOrderServiceApplication.class, OrderRepositoryTest.TestConfig.class})
public class OrderRepositoryTest extends BaseDbIntegrationTest {

  private final Email buyerEmail = new Email("some@email.com");

  @Autowired private ProductRepository productRepository;
  @Autowired private OrderRepository orderRepository;

  @Autowired private TimestampProvider timestampProvider;

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
    var product = new Product(ProductId.generate(), Name.of("milk"), new Price(BigDecimal.TEN));
    productRepository.save(product);
    var products = List.of(product);
    var total = product.price();

    var order =
        new Order(
            OrderId.generate(), products, total, buyerEmail, new OrderTimestamp(Instant.now()));
    var orderSaved = orderRepository.create(order);

    var updatedName = new Name("chocolate");
    var updatedPrice = new Price(BigDecimal.TWO);
    var updatedProduct = new Product(product.productId(), updatedName, updatedPrice);
    productRepository.update(updatedProduct);
    var updatedProductsList = List.of(updatedProduct);
    var nextOrder =
        new Order(
            OrderId.generate(),
            updatedProductsList,
            total,
            buyerEmail,
            new OrderTimestamp(Instant.now()));
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
    var twentyDaysAgo = Instant.now().minus(daysAgo, DAYS).truncatedTo(MILLIS);
    ((TestTimestampProvider) timestampProvider).setFixedTimestamp(twentyDaysAgo);

    var milk = new Product(ProductId.generate(), new Name("milk"), new Price(BigDecimal.TEN));
    var chocolate =
        new Product(ProductId.generate(), new Name("chocolate"), new Price(BigDecimal.TWO));
    productRepository.save(milk);
    productRepository.save(chocolate);
    var productsEarlyOrder = List.of(milk);
    var earlyOrder =
        new Order(
            null, productsEarlyOrder, milk.price(), buyerEmail, new OrderTimestamp(Instant.now()));
    var orderSavedEarly = orderRepository.create(earlyOrder);

    ((TestTimestampProvider) timestampProvider).setFixedTimestamp(Instant.now());

    var laterOrder =
        new Order(
            null, productsEarlyOrder, milk.price(), buyerEmail, new OrderTimestamp(Instant.now()));
    var savedLaterOrder = orderRepository.create(laterOrder);
    var to15daysAgo = twentyDaysAgo.plus(5, DAYS);
    // WHEN Search is requested with specified time range
    var earliestOrders =
        orderRepository.findWithTimeRange(twentyDaysAgo.minus(5, SECONDS), to15daysAgo);

    // THEN Only orders within specified time range are returned
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
