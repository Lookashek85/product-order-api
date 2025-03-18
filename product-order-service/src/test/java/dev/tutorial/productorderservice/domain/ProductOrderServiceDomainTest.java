package dev.tutorial.productorderservice.domain;

// Add tests for validation
class ProductOrderServiceDomainTest {

  //  private ProductService productService;
  //  private OrderService orderService;
  //  private OrderRepository orderRepository;
  //  private ProductRepository productRepository;
  //
  //  private TestTimestampProvider testTimestampProvider;
  //
  //  private final User user =
  //      new User(UserId.generate(), new Name("Lukas"), new Email("lukas@gmail.com"));
  //
  //  public ProductOrderServiceDomainTest() {
  //    this.productService = new ProductServiceDomainImpl();
  //    this.testTimestampProvider = new TestTimestampProvider();
  //    this.orderService = new OrderServiceDomainImpl(productService, testTimestampProvider);
  //    this.orderRepository = new OrderRepositoryImpl();
  //  }
  //
  //  @BeforeEach
  //  public void setUp() {
  //    this.productService =
  //        new ProductServiceImpl(new ProductDomainRepositoryImpl(Collections.emptyList()));
  //    this.testTimestampProvider = new TestTimestampProvider();
  //    this.orderService = new OrderServiceImpl(productService, testTimestampProvider);
  //  }
  //
  //  @Disabled
  //  @Test
  //  void testCreateOrdersWithValidInputs() {
  //    var productNames = List.of("chocolate", "bread", "juice", "eggs");
  //    var price = new BigDecimal(10);
  //    List<Product> productList = createListOfProducts(productNames, new BigDecimal(10));
  //    var order = orderService.createOrder(user, productList);
  //
  //    Assertions.assertNotNull(order);
  //    Assertions.assertEquals(user.email(), order.buyerEmail());
  //    Assertions.assertIterableEquals(productList, order.products());
  //    var expectedTotalPrice = price.multiply(new BigDecimal(productList.size()));
  //    Assertions.assertEquals(0, expectedTotalPrice.compareTo(order.totalPrice().getValue()));
  //  }
  //
  //  @Disabled
  //  @Test
  //  void testCreateOrdersWithUpdatedProduct() {
  //    //  GIVEN existing product is added order
  //    var productName = "Cookies";
  //    var updatedProductName = "Chocolate Cookies";
  //    var product = createProduct(productName, BigDecimal.TEN);
  //    List<Product> productList = new ArrayList<>();
  //    productList.add(product);
  //    var orderOne = orderService.createOrder(user, productList);
  //    // WHEN order is created and product is updated later
  //    var updatedProduct =
  //        new Product(
  //            product.productId(), new Name(updatedProductName), new Price(new BigDecimal("50")));
  //    productService.updateProduct(
  //        new UpdateCreateProductCommand(
  //            updatedProduct.productName(), updatedProduct.price(), updatedProduct.productId()));
  //    var updatedProductList = Collections.singletonList(updatedProduct);
  //    var orderTwo = orderService.createOrder(user, updatedProductList);
  //    var expectedTotalPriceOrderOne = BigDecimal.TEN;
  //    var expectedTotalPriceOrderTwo = new BigDecimal("50.00");
  //
  //    // THEN later order should contain updated product while first order remains unchanged.
  //
  //    Assertions.assertIterableEquals(productList, orderOne.products());
  //    Assertions.assertEquals(
  //        0, expectedTotalPriceOrderOne.compareTo(orderOne.totalPrice().getValue()));
  //
  //    Assertions.assertIterableEquals(updatedProductList, orderTwo.products());
  //    Assertions.assertEquals(
  //        0, expectedTotalPriceOrderTwo.compareTo(orderTwo.totalPrice().getValue()));
  //  }
  //
  //  @Disabled
  //  @Test
  //  void testReturnOrdersWithSpecifiedTimeRange() {
  //    // GIVEN orders were created on different dates
  //    var daysAgo = 20;
  //
  //    Instant twentyDaysAgo = Instant.now().minus(daysAgo, DAYS);
  //    testTimestampProvider.setFixedTimestamp(new OrderTimestamp(twentyDaysAgo));
  //
  //    var productNames = List.of("chocolate");
  //    var price = new BigDecimal(10);
  //    List<Product> productList = createListOfProducts(productNames, price);
  //
  //    var orderOne = orderService.createOrder(user, productList);
  //    var orderTwo = orderService.createOrder(user, productList);
  //
  //    testTimestampProvider.setFixedTimestamp(null); // now
  //    var orderFour = orderService.createOrder(user, productList);
  //    var orderFive = orderService.createOrder(user, productList);
  //    var orderSix = orderService.createOrder(user, productList);
  //
  //    var expectedFoundOrders = List.of(orderFour, orderFive, orderSix);
  //
  //    // WHEN search with specific dates is applied
  //    var ordersFrom10daysAgoToNow =
  //        orderService.getOrders(
  //            new OrderTimestamp(Instant.now().minus(10, DAYS)), new
  // OrderTimestamp(Instant.now()));
  //    // THEN only orders within given time range are returned and rest are discarded
  //    Assertions.assertIterableEquals(ordersFrom10daysAgoToNow, expectedFoundOrders);
  //
  //    var expectedFoundEarliestOrders = List.of(orderOne, orderTwo);
  //    var from20daysAgo = new OrderTimestamp(Instant.now().minus(20, DAYS));
  //    var to15daysAgo = new OrderTimestamp(Instant.now().minus(15, DAYS));
  //    var ordersFrom20daysAgo = orderService.getOrders(from20daysAgo, to15daysAgo);
  //
  //    Assertions.assertIterableEquals(ordersFrom20daysAgo, expectedFoundEarliestOrders);
  //  }
  //
  //  private List<Product> createListOfProducts(List<String> names, BigDecimal price) {
  //    var products = new ArrayList<Product>();
  //    names.forEach(
  //        name -> {
  //          var prod = createProduct(name, price);
  //          products.add(prod);
  //        });
  //    return products;
  //  }
  //
  //  private Product createProduct(String productName, BigDecimal price) {
  //    return new Product(ProductId.generate(), new Name(productName), new Price(price));
  //  }
  //
  //  private static class TestTimestampProvider implements TimestampProvider {
  //    private OrderTimestamp fixedOrderTimestamp;
  //
  //    void setFixedTimestamp(OrderTimestamp orderTimestamp) {
  //      this.fixedOrderTimestamp = orderTimestamp;
  //    }
  //
  //    @Override
  //    public OrderTimestamp now() {
  //      return fixedOrderTimestamp != null ? fixedOrderTimestamp : new
  // OrderTimestamp(Instant.now());
  //    }
  //  }
}
