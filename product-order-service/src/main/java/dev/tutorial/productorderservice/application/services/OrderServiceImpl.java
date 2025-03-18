package dev.tutorial.productorderservice.application.services;

import dev.tutorial.productorderservice.domain.commands.CreateOrderCommand;
import dev.tutorial.productorderservice.domain.core.DomainError;
import dev.tutorial.productorderservice.domain.core.Order;
import dev.tutorial.productorderservice.domain.core.Product;
import dev.tutorial.productorderservice.domain.core.valueobjects.OrderId;
import dev.tutorial.productorderservice.domain.core.valueobjects.OrderTimestamp;
import dev.tutorial.productorderservice.domain.core.valueobjects.Price;
import dev.tutorial.productorderservice.domain.services.OrderService;
import dev.tutorial.productorderservice.domain.services.ProductService;
import dev.tutorial.productorderservice.domain.services.repositories.OrderRepository;
import dev.tutorial.productorderservice.utils.TimestampProvider;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
  private final TimestampProvider timestampProvider;

  private final OrderRepository orderRepository;
  private final ProductService productService;

  public OrderServiceImpl(
      OrderRepository orderRepository,
      ProductService productService,
      TimestampProvider timestampProvider) {
    this.timestampProvider = timestampProvider;
    this.orderRepository = orderRepository;
    this.productService = productService;
  }

  @Override
  public Order createOrder(CreateOrderCommand createOrderCommand) throws DomainError {
    var products = productService.getProductsByIds(createOrderCommand.productIds());
    validateOrderProducts(products);
    var total = calculateTotalOrderPrice(products);
    var order =
        new Order(
            OrderId.generate(),
            products,
            total,
            createOrderCommand.buyerEmail(),
            timestampProvider.now());
    orderRepository.create(order);
    return order;
  }

  @Override
  public List<Order> getOrders(OrderTimestamp from, OrderTimestamp to) {
    validateFromTo(from, to);
    return orderRepository.findWithTimeRange(from.value(), to.value());
  }

  private void validateOrderProducts(List<Product> orderProducts) {
    if (orderProducts == null || orderProducts.isEmpty()) {
      throw new DomainError(
          Product.class.getName(), "Products list not provided when creating an order!");
    }
    validateOrderProductsExist(orderProducts);
  }

  private void validateOrderProductsExist(List<Product> orderProducts) {
    orderProducts.forEach(
        product -> {
          if (!productService.exists(product.productId())) {
            throw new DomainError(
                OrderService.class.getSimpleName(),
                String.format(
                    "Product with id  %s not found  when creating order!", product.productId()));
          }
        });
  }

  private void validateFromTo(OrderTimestamp from, OrderTimestamp to) {
    if (from == null || to == null) {
      throw new DomainError(
          Order.class.getName(),
          "From or to timestamps must not be provided when requesting list of orders!");
    }
    if (from.value().isAfter(to.value()) || to.value().isBefore(from.value())) {
      throw new DomainError(
          Order.class.getName(), "Provided Timestamps are invalid when requesting list of orders!");
    }
  }

  private Price calculateTotalOrderPrice(List<Product> products) {
    return products.stream().map(Product::price).reduce(Price.ZERO, Price::add);
  }
}
