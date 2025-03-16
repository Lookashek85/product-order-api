package dev.tutorial.productorderservice.domain.services;

import dev.tutorial.productorderservice.domain.core.Product;
import dev.tutorial.productorderservice.domain.core.valueobjects.ProductId;
import java.util.List;
import java.util.Optional;

public interface ProductService {
  Product createProduct(Product product);

  List<Product> getProducts();

  Boolean exists(ProductId productId);

  Optional<Product> getProductById(ProductId productId);

  Product updateProduct(Product product);
}
