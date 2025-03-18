package dev.tutorial.productorderservice.domain.core;

import dev.tutorial.productorderservice.domain.core.valueobjects.Name;
import dev.tutorial.productorderservice.domain.core.valueobjects.Price;
import dev.tutorial.productorderservice.domain.core.valueobjects.ProductId;

import java.util.Objects;

public class Product {
  private ProductId productId;
  private Name productName;
  Price price;

  public Product(ProductId productId, Name productName, Price price) {
    this.productId = productId;
    this.productName = productName;
    this.price = price;
  }

  public ProductId productId() {
    return productId;
  }

  public void setProductId(ProductId productId) {
    this.productId = productId;
  }

  public Name productName() {
    return productName;
  }

  public void setProductName(Name productName) {
    this.productName = productName;
  }

  public Price price() {
    return price;
  }

  public void setPrice(Price price) {
    this.price = price;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    Product product = (Product) o;
    return Objects.equals(productId, product.productId)
        && Objects.equals(productName, product.productName)
        && Objects.equals(price, product.price);
  }

  @Override
  public int hashCode() {
    return Objects.hash(productId, productName, price);
  }
}
