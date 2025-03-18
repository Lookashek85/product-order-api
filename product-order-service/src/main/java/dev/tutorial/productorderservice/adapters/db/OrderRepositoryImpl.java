package dev.tutorial.productorderservice.adapters.db;

import dev.tutorial.productorderservice.adapters.db.model.OrderDb;
import dev.tutorial.productorderservice.adapters.db.model.ProductDb;
import dev.tutorial.productorderservice.domain.core.Order;
import dev.tutorial.productorderservice.domain.core.Product;
import dev.tutorial.productorderservice.domain.services.repositories.OrderRepository;
import dev.tutorial.productorderservice.domain.services.repositories.ProductRepository;
import dev.tutorial.productorderservice.utils.TimestampProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

  private final Logger log = LoggerFactory.getLogger(OrderRepositoryImpl.class);

  private final ProductRepository productRepository;
  private final OrdersJpaRepository ordersJpaRepository;
  private final TimestampProvider timestampProvider;

  public OrderRepositoryImpl(
      ProductRepository productRepository,
      OrdersJpaRepository ordersJpaRepository,
      TimestampProvider timestampProvider) {
    this.productRepository = productRepository;
    this.ordersJpaRepository = ordersJpaRepository;
    this.timestampProvider = timestampProvider;
  }

  @Override
  public Order create(Order order) {
    log.debug("Creating order {}", order);
    var orderDb = OrderDb.toDb(order);
    orderDb.setOrderTimestamp(Timestamp.from(timestampProvider.now().value()));
    var productIds = order.products().stream().map(Product::productId).toList();
    var products = productRepository.findAllByIds(productIds);
    orderDb.setProducts(products.stream().map(ProductDb::toDb).collect(Collectors.toList()));
    var savedOrderDb = ordersJpaRepository.save(orderDb);
    return OrderDb.toDomain(savedOrderDb);
  }

  @Override
  public List<Order> findWithTimeRange(Instant from, Instant to) {
    return ordersJpaRepository
        .findAllWithTimeRange(Timestamp.from(from), Timestamp.from(to))
        .stream()
        .map(OrderDb::toDomain)
        .toList();
  }

  @Override
  public void deleteAll() {
    ordersJpaRepository.deleteAll();
  }
}
