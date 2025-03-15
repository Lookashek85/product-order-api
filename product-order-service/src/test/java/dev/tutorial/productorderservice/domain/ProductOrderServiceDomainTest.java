package dev.tutorial.productorderservice.domain;

import static java.time.temporal.ChronoUnit.DAYS;

import dev.tutorial.productorderservice.domain.core.OrderService;
import dev.tutorial.productorderservice.domain.core.Product;
import dev.tutorial.productorderservice.domain.core.ProductService;
import dev.tutorial.productorderservice.domain.core.User;
import dev.tutorial.productorderservice.domain.core.valueobjects.Email;
import dev.tutorial.productorderservice.domain.core.valueobjects.Name;
import dev.tutorial.productorderservice.domain.core.valueobjects.Price;
import dev.tutorial.productorderservice.domain.core.valueobjects.ProductId;
import dev.tutorial.productorderservice.domain.core.valueobjects.Timestamp;
import dev.tutorial.productorderservice.domain.core.valueobjects.UserId;
import dev.tutorial.productorderservice.domain.services.OrderServiceImpl;
import dev.tutorial.productorderservice.domain.services.ProductServiceImpl;
import dev.tutorial.productorderservice.utils.TimestampProvider;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

// Add tests for validation
class ProductOrderServiceDomainTest {

  private ProductService productService;
  private OrderService orderService;

  private TestTimestampProvider testTimestampProvider;

  private final User user =
      new User(UserId.generate(), new Name("Lukas"), new Email("lukas@gmail.com"));

  public ProductOrderServiceDomainTest() {
    this.productService = new ProductServiceImpl();
    this.testTimestampProvider = new TestTimestampProvider();
    this.orderService = new OrderServiceImpl(productService, testTimestampProvider);
  }

  @BeforeEach
  public void setUp() {
    this.productService = new ProductServiceImpl();
    this.testTimestampProvider = new TestTimestampProvider();
    this.orderService = new OrderServiceImpl(productService, testTimestampProvider);
  }

  @Test
  void testCreateOrdersWithValidInputs() {
    var productNames = List.of("chocolate", "bread", "juice", "eggs");
    var price = new BigDecimal(10);
    List<Product> productList = createListOfProducts(productNames, new BigDecimal(10));
    var order = orderService.createOrder(user, productList);

    Assertions.assertNotNull(order);
    Assertions.assertEquals(user.email(), order.buyerEmail());
    Assertions.assertIterableEquals(productList, order.products());
    var expectedTotalPrice = price.multiply(new BigDecimal(productList.size()));
    Assertions.assertTrue(expectedTotalPrice.compareTo(order.totalPrice().getValue()) == 0);
  }

  @Test
  void testCreateOrdersWithUpdatedProduct() {
    //  GIVEN existing product is added order
    var productName = "Cookies";
    var updatedProductName = "Chocolate Cookies";
    var product = createProduct(productName, BigDecimal.TEN);
    var productList = Collections.singletonList(product);
    var orderOne = orderService.createOrder(user, productList);
    // WHEN order is created and product is updated later
    var updatedProduct =
        new Product(
            product.productId(), new Name(updatedProductName), new Price(new BigDecimal("50")));
    productService.updateProduct(updatedProduct);
    var updatedProductList = Collections.singletonList(updatedProduct);
    var orderTwo = orderService.createOrder(user, updatedProductList);
    var expectedTotalPriceOrderOne = BigDecimal.TEN;
    var expectedTotalPriceOrderTwo = new BigDecimal("50.00");

    // THEN later order should contain updated product while first order remains unchanged.

    Assertions.assertIterableEquals(productList, orderOne.products());
    Assertions.assertTrue(
        expectedTotalPriceOrderOne.compareTo(orderOne.totalPrice().getValue()) == 0);

    Assertions.assertIterableEquals(updatedProductList, orderTwo.products());
    Assertions.assertTrue(
        expectedTotalPriceOrderTwo.compareTo(orderTwo.totalPrice().getValue()) == 0);
  }

  @Test
  void testReturnOrdersWithSpecifiedTimeRange() {
    // GIVEN orders were created on different dates
    var daysAgo = 20;

    Instant twentyDaysAgo = Instant.now().minus(daysAgo, DAYS);
    testTimestampProvider.setFixedTimestamp(new Timestamp(twentyDaysAgo));

    var productNames = List.of("chocolate");
    var price = new BigDecimal(10);
    List<Product> productList = createListOfProducts(productNames, price);

    var orderOne = orderService.createOrder(user, productList);
    var orderTwo = orderService.createOrder(user, productList);
    var orderThree = orderService.createOrder(user, productList);

    testTimestampProvider.setFixedTimestamp(null); // now
    var orderFour = orderService.createOrder(user, productList);
    var orderFive = orderService.createOrder(user, productList);
    var orderSix = orderService.createOrder(user, productList);

    var expectedFoundOrders = List.of(orderFour, orderFive, orderSix);

    // WHEN search with specific dates is applied
    var ordersFrom10daysAgoToNow =
        orderService.getOrders(
            new Timestamp(Instant.now().minus(10, DAYS)), new Timestamp(Instant.now()));

    // THEN only orders within given time range are returned and rest are discarded
    Assertions.assertIterableEquals(ordersFrom10daysAgoToNow, expectedFoundOrders);
    var from20daysAgo = new Timestamp(Instant.now().minus(20, DAYS));
    var to15daysAgo = new Timestamp(Instant.now().minus(15, DAYS));
    var expectedFoundPastOrders = List.of(orderOne, orderTwo, orderThree);
    var ordersFrom20daysAgo = orderService.getOrders(from20daysAgo, to15daysAgo);
    // TODO FIX CODE LOGIC
    //    Assertions.assertIterableEquals(ordersFrom20daysAgo, expectedFoundPastOrders);
  }

  private List<Product> createListOfProducts(List<String> names, BigDecimal price) {
    var products = new ArrayList<Product>();
    names.forEach(
        name -> {
          var prod = createProduct(name, price);
          products.add(prod);
        });
    return products;
  }

  private Product createProduct(String productName, BigDecimal price) {
    var prod = new Product(ProductId.generate(), new Name(productName), new Price(price));
    productService.createProduct(prod);
    return prod;
  }

  private static class TestTimestampProvider implements TimestampProvider {
    private Timestamp fixedTimestamp;

    void setFixedTimestamp(Timestamp timestamp) {
      this.fixedTimestamp = timestamp;
    }

    @Override
    public Timestamp now() {
      return fixedTimestamp != null ? fixedTimestamp : new Timestamp(Instant.now());
    }
  }
}
