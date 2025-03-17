package dev.tutorial.productorderservice.adapters.db.model;

import dev.tutorial.productorderservice.domain.core.Product;
import dev.tutorial.productorderservice.domain.core.valueobjects.Name;
import dev.tutorial.productorderservice.domain.core.valueobjects.Price;
import dev.tutorial.productorderservice.domain.core.valueobjects.ProductId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Table(name = "product")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ProductDb {
  @Id private UUID id;

  private String name;
  private BigDecimal price;

  public static Product toDomain(ProductDb productDb) {
    return new Product(
        ProductId.fromUUID(productDb.getId()),
        new Name(productDb.getName()),
        new Price(productDb.getPrice()));
  }

  public static ProductDb toDb(Product product) {
    return new ProductDb(product.productId().getValue(), product.productName().value(), product.price().getValue());
  }
}
