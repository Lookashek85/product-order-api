package dev.tutorial.productorderservice.adapters.db.model;

import dev.tutorial.productorderservice.domain.core.Order;
import dev.tutorial.productorderservice.domain.core.valueobjects.Email;
import dev.tutorial.productorderservice.domain.core.valueobjects.OrderId;
import dev.tutorial.productorderservice.domain.core.valueobjects.OrderTimestamp;
import dev.tutorial.productorderservice.domain.core.valueobjects.Price;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Table(name = "orders")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class OrderDb {
  @Id private UUID id;

  private String buyerEmail;
  private BigDecimal totalValue;
  private Timestamp orderTimestamp;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "order_product",
      joinColumns = @JoinColumn(name = "order_id"),
      inverseJoinColumns = @JoinColumn(name = "product_id"))
  private List<ProductDb> products;

  public static OrderDb toDb(Order order) {
    var id = order.orderId() != null ? order.orderId().getValue() : UUID.randomUUID();
    var buyerEmail = order.buyerEmail().value();
    var totalValue = order.totalPrice().getValue();
    // overwritten in repo by timestampProvider
    var orderTimestamp = Timestamp.from(order.orderTimestamp().value());
    var products = order.products().stream().map(ProductDb::toDb).toList();

    return new OrderDb(id, buyerEmail, totalValue, orderTimestamp, products);
  }

  public static Order toDomain(OrderDb orderDb) {
    var orderId = OrderId.fromUUID(orderDb.getId());
    var buyerEmail = new Email(orderDb.getBuyerEmail());
    var totalPrice = new Price(orderDb.getTotalValue());
    var orderTimestamp =
        new OrderTimestamp(Instant.ofEpochMilli(orderDb.getOrderTimestamp().getTime()));

    var products = orderDb.getProducts().stream().map(ProductDb::toDomain).toList();

    return new Order(orderId, products, totalPrice, buyerEmail, orderTimestamp);
  }
}
