package dev.tutorial.productorderservice.domain.services;

import dev.tutorial.productorderservice.domain.core.DomainError;
import dev.tutorial.productorderservice.domain.core.Product;
import dev.tutorial.productorderservice.domain.core.ProductService;
import dev.tutorial.productorderservice.domain.core.valueobjects.ProductId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductServiceImpl implements ProductService {
  private final List<Product> products;

  public ProductServiceImpl() {
    this.products = new ArrayList<>();
  }

  @Override
  public Product createProduct(Product product) {
    // todo domain specific errors
    if (product == null) {
      throw new DomainError(Product.class.getName(), "No product specified!");
    }
    if (products.contains(product)) {
      throw new DomainError(Product.class.getName(), "Product already exists!");
    }
    products.add(product);
    return product;
  }

  @Override
  public List<Product> getProducts() {
    return products;
  }

  @Override
  public Boolean exists(ProductId productId) {
    return products.stream().anyMatch(pr -> pr.productId().equals(productId));
  }

  @Override
  public Optional<Product> getProductById(ProductId productId) {
    var product = products.stream().filter(pr -> pr.productId().equals(productId)).findFirst();
    if (product.isEmpty()) {
      System.out.printf("No product found with ID %s%n", productId);
    }
    return product;
  }

  @Override
  public Product updateProduct(Product updatedProduct) {

    if (!products.stream().anyMatch(pt -> pt.productId().equals(updatedProduct.productId()))) {
      throw new DomainError(
          Product.class.getName(),
          "No product with Id= " + updatedProduct.productId() + " exists for update! ");
    }
    products.removeIf(p -> p.productId().equals(updatedProduct.productId()));
    products.add(updatedProduct);
    return updatedProduct;
  }
}
