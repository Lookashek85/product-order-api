package dev.tutorial.productorderservice.adapters.db;

import dev.tutorial.productorderservice.adapters.db.model.ProductDb;
import dev.tutorial.productorderservice.domain.core.DomainError;
import dev.tutorial.productorderservice.domain.core.Product;
import dev.tutorial.productorderservice.domain.core.valueobjects.BaseId;
import dev.tutorial.productorderservice.domain.core.valueobjects.ProductId;
import dev.tutorial.productorderservice.domain.services.repositories.ProductRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

  private final Logger log = LoggerFactory.getLogger(ProductRepositoryImpl.class);

  private final ProductJpaRepository productJpaRepository;

  public ProductRepositoryImpl(ProductJpaRepository productJpaRepository) {
    this.productJpaRepository = productJpaRepository;
  }

  @Override
  public Product save(Product product) {
    log.debug("Saving product {}", product);
    var dbProduct = productJpaRepository.save(ProductDb.toDb(product));
    return ProductDb.toDomain(dbProduct);
  }

  @Override
  public Product update(Product product) {
    log.debug("Updating product {}", product);
    var currentDbProduct =
        productJpaRepository
            .findById(product.productId().getValue())
            .orElseThrow(
                () ->
                    new DomainError(
                        ProductDb.class.getName(),
                        "Existing product for update not found using id=" + product.productId()));

    currentDbProduct.setName(product.productName().value());
    currentDbProduct.setPrice(product.price().getValue());
    var updatedDbProduct = productJpaRepository.save(currentDbProduct);

    log.debug(
        "Success updating old product {} to new product {}", currentDbProduct, updatedDbProduct);

    return ProductDb.toDomain(updatedDbProduct);
  }

  @Override
  public Optional<Product> findById(ProductId id) {
    return productJpaRepository.findById(id.getValue()).map(ProductDb::toDomain);
  }

  @Override
  public List<Product> findAll() {
    return productJpaRepository.findAll().stream().map(ProductDb::toDomain).toList();
  }

  @Override
  public List<Product> findAllByIds(List<ProductId> ids) {
    return productJpaRepository.findAllById(ids.stream().map(BaseId::getValue).toList()).stream()
        .map(ProductDb::toDomain)
        .toList();
  }

  @Override
  public void deleteAll() {
    productJpaRepository.deleteAll();
  }
}
