package dev.tutorial.productorderservice.domain.core;

import dev.tutorial.productorderservice.domain.core.valueobjects.Name;
import dev.tutorial.productorderservice.domain.core.valueobjects.Price;
import dev.tutorial.productorderservice.domain.core.valueobjects.ProductId;

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


}
