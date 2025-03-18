package dev.tutorial.productorderservice.adapters.http.api;

import dev.tutorial.productorderservice.adapters.http.requests.ProductRequest;
import dev.tutorial.productorderservice.adapters.http.requests.UpdateProductRequest;
import dev.tutorial.productorderservice.adapters.http.responses.ProductResponse;
import dev.tutorial.productorderservice.adapters.http.responses.ProductsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Products", description = "Product API ")
public interface ProductsOpenApi {

  @Operation(summary = "Create new product", description = "Registers a new product in the system")
  @ApiResponses({
    @ApiResponse(
        responseCode = "201",
        description = "Product successfully created",
        content = @Content(schema = @Schema(implementation = ProductResponse.class))),
    @ApiResponse(responseCode = "400", description = "Invalid product data"),
    @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  ResponseEntity<ProductResponse> create(
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "Product creation data",
              required = true,
              content = @Content(schema = @Schema(implementation = ProductRequest.class)))
          @Valid
          @RequestBody
          ProductRequest request);

  @Operation(
      summary = "Update existing product",
      description = "Updates details of an existing product")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Product successfully updated",
        content = @Content(schema = @Schema(implementation = ProductResponse.class))),
    @ApiResponse(responseCode = "400", description = "Invalid update data"),
    @ApiResponse(responseCode = "404", description = "Product not found"),
    @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  ResponseEntity<ProductResponse> update(
      @Parameter(
              description = "ID of the product to update",
              example = "prod-12345",
              required = true)
          @PathVariable
          String productId,
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "Product update data",
              required = true,
              content = @Content(schema = @Schema(implementation = UpdateProductRequest.class)))
          @RequestBody
          UpdateProductRequest request);

  @Operation(
      summary = "List all products",
      description = "Retrieves a list of all available products")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Successfully retrieved product list",
        content = @Content(schema = @Schema(implementation = ProductsResponse.class))),
    @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  ResponseEntity<ProductsResponse> findProducts();
}
