package dev.tutorial.productorderservice.domain.commands;

import dev.tutorial.productorderservice.domain.core.valueobjects.Name;
import dev.tutorial.productorderservice.domain.core.valueobjects.Price;
import java.util.Objects;

public class CreateProductCommand {
  private Name productName;
  private Price price;

  public CreateProductCommand(Name productName, Price price) {
    this.productName = productName;
    this.price = price;
  }

  public Name getProductName() {
    return productName;
  }

  public void setProductName(Name productName) {
    this.productName = productName;
  }

  public Price getPrice() {
    return price;
  }

  public void setPrice(Price price) {
    this.price = price;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CreateProductCommand that = (CreateProductCommand) o;
    return Objects.equals(productName, that.productName) && Objects.equals(price, that.price);
  }

  @Override
  public int hashCode() {
    return Objects.hash(productName, price);
  }

  @Override
  public String toString() {
    return "ProductCommand{" + "productName='" + productName + '\'' + ", price=" + price + '}';
  }
}
