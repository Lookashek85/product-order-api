package dev.tutorial.productorderservice.application.services;

import dev.tutorial.productorderservice.domain.commands.CreateProductCommand;
import dev.tutorial.productorderservice.domain.commands.UpdateProductCommand;
import dev.tutorial.productorderservice.domain.core.DomainError;
import dev.tutorial.productorderservice.domain.core.Product;
import dev.tutorial.productorderservice.domain.core.ProductNotFound;
import dev.tutorial.productorderservice.domain.core.valueobjects.ProductId;
import dev.tutorial.productorderservice.domain.services.ProductService;
import dev.tutorial.productorderservice.domain.services.repositories.ProductRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductServiceImpl implements ProductService {

  private final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

  private final ProductRepository productRepository;

  public ProductServiceImpl(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  @Override
  public Product createProduct(CreateProductCommand command) {
    var productName = command.getProductName();
    if (productRepository.existsByName(productName)) {
      throw new DomainError(Product.class.getName(), "Product already exists!");
    }
    var product = fromCommand(command);
    var id = product.productId() != null ? product.productId().getValue() : UUID.randomUUID();
    product.setProductId(ProductId.fromUUID(id));
    log.info("Creating product {}", product);
    var savedProduct = productRepository.save(product);
    log.info("Saved product {}", savedProduct);
    return product;
  }

  @Override
  public List<Product> getProducts() {
    return productRepository.findAll();
  }

  @Override
  public List<Product> getProductsByIds(List<ProductId> productIds) {
    return productRepository.findAllByIds(productIds);
  }

  @Override
  public Boolean exists(ProductId productId) {
    return productRepository.existsById(productId);
  }

  @Override
  public Optional<Product> getProductById(ProductId productId) {
    var product = productRepository.findById(productId);
    if (product.isEmpty()) {
      log.info("Product not found by id {}", productId);
    }
    return productRepository.findById(productId);
  }

  @Override
  @Transactional
  public Product updateProduct(UpdateProductCommand updateProductCommand) {
    var updatedProduct = fromCommand(updateProductCommand);
    log.info("Updating product {}", updatedProduct);
    if (!productRepository.existsById(updatedProduct.productId())) {
      throw new ProductNotFound(
          "No product with Id= " + updatedProduct.productId() + " exists for update! ");
    }
    var updated = productRepository.update(updatedProduct);
    log.info("Updated product {}", updated);
    return updated;
  }

  private Product fromCommand(CreateProductCommand command) {
    return new Product(null, command.getProductName(), command.getPrice());
  }

  private Product fromCommand(UpdateProductCommand command) {
    return new Product(command.getProductId(), command.getProductName(), command.getPrice());
  }
}
