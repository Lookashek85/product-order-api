package dev.tutorial.productorderservice.adapters.http.responses;

import dev.tutorial.productorderservice.domain.core.Product;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductsResponse {
  private List<ProductResponse> products;

  public static ProductsResponse of(List<Product> products) {
    return new ProductsResponse(products.stream().map(ProductResponse::of).toList());
  }
}
