package dev.tutorial.productorderservice.domain.services;

import dev.tutorial.productorderservice.domain.commands.CreateProductCommand;
import dev.tutorial.productorderservice.domain.commands.UpdateProductCommand;
import dev.tutorial.productorderservice.domain.core.DomainError;
import dev.tutorial.productorderservice.domain.core.Product;
import dev.tutorial.productorderservice.domain.core.valueobjects.ProductId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductServiceDomainImpl implements ProductService {
  private final List<Product> products;

  public ProductServiceDomainImpl() {
    this.products = new ArrayList<>();
  }

  @Override
  public Product createProduct(CreateProductCommand createProductCommand) {
    // todo domain specific errors
    if (createProductCommand == null) {
      throw new DomainError(Product.class.getName(), "No product specified!");
    }
    var product =
        new Product(
            ProductId.generate(),
            createProductCommand.getProductName(),
            createProductCommand.getPrice());
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
  public List<Product> getProductsByIds(List<ProductId> productIds) {
    return List.of();
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
  public Product updateProduct(UpdateProductCommand productCommand) {
    var updatedProduct =
        new Product(
            productCommand.getProductId(),
            productCommand.getProductName(),
            productCommand.getPrice());
    if (products.stream().noneMatch(pt -> pt.productId().equals(updatedProduct.productId()))) {
      throw new DomainError(
          Product.class.getName(),
          "No product with Id= " + updatedProduct.productId() + " exists for update! ");
    }
    products.removeIf(p -> p.productId().equals(updatedProduct.productId()));
    products.add(updatedProduct);
    return updatedProduct;
  }
}
