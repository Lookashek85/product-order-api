package dev.tutorial.productorderservice.adapters.http;

import dev.tutorial.productorderservice.adapters.http.requests.ProductRequest;
import dev.tutorial.productorderservice.adapters.http.requests.UpdateProductRequest;
import dev.tutorial.productorderservice.adapters.http.responses.ProductResponse;
import dev.tutorial.productorderservice.adapters.http.responses.ProductsResponse;
import dev.tutorial.productorderservice.domain.commands.ProductCommand;
import dev.tutorial.productorderservice.domain.commands.UpdateProductCommand;
import dev.tutorial.productorderservice.domain.core.valueobjects.Name;
import dev.tutorial.productorderservice.domain.core.valueobjects.Price;
import dev.tutorial.productorderservice.domain.core.valueobjects.ProductId;
import dev.tutorial.productorderservice.domain.services.ProductService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/products")
public class ProductsRouter {

  private final Logger log = LoggerFactory.getLogger(ProductsRouter.class);

  private final ProductService productService;

  public ProductsRouter(ProductService productService) {
    this.productService = productService;
  }

  @PostMapping
  public ResponseEntity<ProductResponse> create(@RequestBody @Valid ProductRequest request) {
    log.info(" POST /v1/products request: {}", request);
    var ret = productService.createProduct(toCommand(request));
    var product = ProductResponse.of(ret);
    return ResponseEntity.status(HttpStatus.CREATED).body(product);
  }

  @PutMapping("/{productId}")
  public ResponseEntity<ProductResponse> update(@PathVariable String productId,
                                                @RequestBody @Valid UpdateProductRequest request) {
    log.info(" PUT /v1/products/{}  request: {}", productId, request);
    var product = ProductResponse.of(productService.updateProduct(toCommand(request)));
    return ResponseEntity.status(HttpStatus.OK).body(product);
  }

  @GetMapping()
  public ResponseEntity<ProductsResponse> findProducts() {
    log.info("GET /v1/products");
    var products = productService.getProducts();
    return ResponseEntity.ok(ProductsResponse.of(products));
  }

  private ProductCommand toCommand(ProductRequest request) {
    return new ProductCommand(new Name(request.getProductName()), new Price(request.getPrice()));
  }

  private UpdateProductCommand toCommand(UpdateProductRequest request) {
    return new UpdateProductCommand(
        new Name(request.getProductName()),
        new Price(request.getPrice()),
        ProductId.fromString(request.getProductId()));
  }
}
