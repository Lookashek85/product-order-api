package dev.tutorial.productorderservice.domain.services;

import dev.tutorial.productorderservice.domain.commands.CreateProductCommand;
import dev.tutorial.productorderservice.domain.commands.UpdateProductCommand;
import dev.tutorial.productorderservice.domain.core.Product;
import dev.tutorial.productorderservice.domain.core.valueobjects.ProductId;

import java.util.List;
import java.util.Optional;

public interface ProductService {
  Product createProduct(CreateProductCommand command);

  List<Product> getProducts();

  List<Product> getProductsByIds(List<ProductId> productIds);

  Boolean exists(ProductId productId);

  Optional<Product> getProductById(ProductId productId);

  Product updateProduct(UpdateProductCommand command);
}
