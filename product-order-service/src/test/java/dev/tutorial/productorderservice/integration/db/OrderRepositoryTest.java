package dev.tutorial.productorderservice.integration.db;

import static java.time.temporal.ChronoUnit.DAYS;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import dev.tutorial.productorderservice.BaseDbIntegrationTest;
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
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
public class OrderRepositoryTest extends BaseDbIntegrationTest {

  private final Email buyerEmail = new Email("some@email.com");

  @Autowired private ProductRepository productRepository;
  @Autowired private OrderRepository orderRepository;

  private TestTimestampProvider testTimestampProvider;

  @BeforeEach
  void setUp() {
    productRepository.deleteAll();
    orderRepository.deleteAll();
    this.testTimestampProvider = new TestTimestampProvider();
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
    assertThat(orderSaved.products()).isEqualTo(products);
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

//  @Test
//  void shouldRetrieveOrdersByGivenTimeRange() {
//    // GIVEN orders were created on different dates
//    var daysAgo = 20;
//    var twentyDaysAgo = Instant.now().minus(daysAgo, DAYS);
//    testTimestampProvider.setFixedTimestamp(new OrderTimestamp(twentyDaysAgo));
//
//    var milk = new Product(ProductId.generate(), new Name("milk"), new Price(BigDecimal.TEN));
//    var productsEarlyOrder = List.of(milk);
//    productRepository.save(milk);
//    var order = new Order(null, productsEarlyOrder, milk.price(), buyerEmail, new OrderTimestamp(Instant.now()));
//    var orderSaved = orderRepository.create(order);
//
//    testTimestampProvider.setFixedTimestamp(null); // now
//    var nextOrder = orderRepository.create(order);
//  }

  private static class TestTimestampProvider implements TimestampProvider {
    private OrderTimestamp fixedOrderTimestamp;

    void setFixedTimestamp(OrderTimestamp orderTimestamp) {
      this.fixedOrderTimestamp = orderTimestamp;
    }

    @Override
    public OrderTimestamp now() {
      return fixedOrderTimestamp != null ? fixedOrderTimestamp : new OrderTimestamp(Instant.now());
    }
  }
}
