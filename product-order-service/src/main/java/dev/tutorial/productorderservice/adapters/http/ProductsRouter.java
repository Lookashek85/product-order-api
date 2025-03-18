package dev.tutorial.productorderservice.adapters.http;

import dev.tutorial.productorderservice.adapters.http.api.ProductsOpenApi;
import dev.tutorial.productorderservice.adapters.http.requests.ProductRequest;
import dev.tutorial.productorderservice.adapters.http.requests.UpdateProductRequest;
import dev.tutorial.productorderservice.adapters.http.responses.ProductResponse;
import dev.tutorial.productorderservice.adapters.http.responses.ProductsResponse;
import dev.tutorial.productorderservice.domain.commands.CreateProductCommand;
import dev.tutorial.productorderservice.domain.core.valueobjects.Name;
import dev.tutorial.productorderservice.domain.core.valueobjects.Price;
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

// TODO worth considering replacement of PUT with PATCH for partial updates of fields when model
// grows..

@RestController
@RequestMapping("/v1/products")
public class ProductsRouter implements ProductsOpenApi {

  private final Logger log = LoggerFactory.getLogger(ProductsRouter.class);

  private final ProductService productService;

  private final Validators validators;

  public ProductsRouter(ProductService productService, Validators validators) {
    this.productService = productService;
    this.validators = validators;
  }

  @PostMapping
  public ResponseEntity<ProductResponse> create(@RequestBody @Valid ProductRequest request) {
    log.info(" POST /v1/products request: {}", request);
    var product = ProductResponse.of(productService.createProduct(toCreateProductCommand(request)));
    return ResponseEntity.status(HttpStatus.CREATED).body(product);
  }

  @PutMapping("/{productId}")
  public ResponseEntity<ProductResponse> update(
      @PathVariable String productId, @RequestBody UpdateProductRequest request) {
    log.info(" PUT /v1/products/{}  request: {}", productId, request);
    var product =
        ProductResponse.of(
            productService.updateProduct(
                validators.toValidatedUpdateProductCommand(request, productId)));
    return ResponseEntity.status(HttpStatus.OK).body(product);
  }

  @GetMapping()
  public ResponseEntity<ProductsResponse> findProducts() {
    log.info("GET /v1/products");
    var products = productService.getProducts();
    return ResponseEntity.ok(ProductsResponse.of(products));
  }

  private CreateProductCommand toCreateProductCommand(ProductRequest request) {
    return new CreateProductCommand(
        new Name(request.getProductName()), new Price(request.getPrice()));
  }
}
