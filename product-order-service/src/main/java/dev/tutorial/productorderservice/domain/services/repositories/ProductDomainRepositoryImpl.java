package dev.tutorial.productorderservice.domain.services.repositories;

import dev.tutorial.productorderservice.domain.core.DomainError;
import dev.tutorial.productorderservice.domain.core.Product;
import dev.tutorial.productorderservice.domain.core.valueobjects.Name;
import dev.tutorial.productorderservice.domain.core.valueobjects.ProductId;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ProductDomainRepositoryImpl implements ProductRepository {

  private final List<Product> products;

  public ProductDomainRepositoryImpl(List<Product> products) {
    this.products = products;
  }

  @Override
  public Product save(Product product) {
    products.add(product);
    return product;
  }

  @Override
  public Product update(Product product) throws DomainError {
    return product;
  }

  @Override
  public Optional<Product> findById(ProductId id) {
    return products.stream().filter(p -> p.productId().equals(id)).findFirst();
  }

  @Override
  public List<Product> findAll() {
    return products;
  }

  @Override
  public List<Product> findAllByIds(List<ProductId> ids) {
    Set<ProductId> idValues = new HashSet<>(ids);

    return products.stream()
        .filter(product -> idValues.contains(product.productId()))
        .collect(Collectors.toList());
  }

  @Override
  public boolean existsById(ProductId id) {
    return products.stream().anyMatch(product -> product.productId().equals(id));
  }

  @Override
  public boolean existsByName(Name name) {
    return products.stream().anyMatch(product -> product.productName().equals(name));
  }

  @Override
  public void deleteAll() {
    products.clear();
  }
}
