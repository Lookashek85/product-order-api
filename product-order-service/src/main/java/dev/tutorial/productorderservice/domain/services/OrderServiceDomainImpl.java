package dev.tutorial.productorderservice.domain.services;

import dev.tutorial.productorderservice.application.services.ProductServiceImpl;
import dev.tutorial.productorderservice.domain.core.DomainError;
import dev.tutorial.productorderservice.domain.core.Order;
import dev.tutorial.productorderservice.domain.core.Product;
import dev.tutorial.productorderservice.domain.core.User;
import dev.tutorial.productorderservice.domain.core.valueobjects.OrderId;
import dev.tutorial.productorderservice.domain.core.valueobjects.OrderTimestamp;
import dev.tutorial.productorderservice.domain.core.valueobjects.Price;
import dev.tutorial.productorderservice.utils.TimestampProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderServiceDomainImpl implements OrderService {
    private final TimestampProvider timestampProvider;

    private final List<Order> orders;

    private final ProductService productService;

    public OrderServiceDomainImpl(ProductService productService, TimestampProvider timestampProvider) {
        this.timestampProvider = timestampProvider;
        this.orders = new ArrayList<>();
        this.productService = new ProductServiceDomainImpl();
    }

    @Override
    public Order createOrder(User user, List<Product> products) throws DomainError {
        if (user == null) {
            throw new DomainError(User.class.getName(), "User must be present when creating an order!");
        }
        validateOrderProducts(products);
        var total = calculateTotalOrderPrice(products);
        var order =
                new Order(OrderId.generate(), products, total, user.email(), timestampProvider.now());
        orders.add(order);
        return order;
    }

    @Override
    public List<Order> getOrders(OrderTimestamp from, OrderTimestamp to) {
        validateFromTo(from, to);
        return orders.stream()
                .filter(order -> isOrderWithinTimeRange(order, from, to))
                .collect(Collectors.toList());
    }

    private void validateOrderProducts(List<Product> orderProducts) {
        if (orderProducts == null || orderProducts.isEmpty()) {
            throw new DomainError(
                    Product.class.getName(), "Products list not provided when creating an order!");
        }
        validateOrderProductsExist(orderProducts);
    }

    private void validateOrderProductsExist(List<Product> orderProducts) {
        orderProducts.forEach(
                product -> {
                    if (!productService.exists(product.productId())) {
                        throw new DomainError(
                                OrderService.class.getSimpleName(),
                                String.format(
                                        "Product with id  %s not found  when creating order!", product.productId()));
                    }
                });
    }

    // todo make cleaner
    private boolean isOrderWithinTimeRange(Order order, OrderTimestamp from, OrderTimestamp to) {
        return order.orderTimestamp().value().compareTo(from.value()) >= 0
                && order.orderTimestamp().value().compareTo(to.value()) <= 0;
    }

    private void validateFromTo(OrderTimestamp from, OrderTimestamp to) {
        if (from == null || to == null) {
            throw new DomainError(
                    Order.class.getName(),
                    "From or to timestamps must not be provided when requesting list of orders!");
        }
        if (from.value().isAfter(to.value()) || to.value().isBefore(from.value())) {
            throw new DomainError(
                    Order.class.getName(), "Provided Timestamps are invalid when requesting list of orders!");
        }
    }

    private Price calculateTotalOrderPrice(List<Product> products) {
        return products.stream().map(Product::price).reduce(Price.ZERO, Price::add);
    }
}
