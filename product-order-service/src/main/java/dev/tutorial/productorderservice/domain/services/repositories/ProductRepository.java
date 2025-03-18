package dev.tutorial.productorderservice.domain.services.repositories;

import dev.tutorial.productorderservice.domain.core.DomainError;
import dev.tutorial.productorderservice.domain.core.Product;
import dev.tutorial.productorderservice.domain.core.valueobjects.Name;
import dev.tutorial.productorderservice.domain.core.valueobjects.ProductId;
import java.util.List;
import java.util.Optional;

public interface ProductRepository {
  Product save(Product product);

  Product update(Product product) throws DomainError;

  Optional<Product> findById(ProductId id);

  // ok for development to don't use limits, paging, etc...
  List<Product> findAll();

  List<Product> findAllByIds(List<ProductId> ids);

  boolean existsById(ProductId id);

  boolean existsByName(Name name);

  void deleteAll();
}
