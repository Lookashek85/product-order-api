CREATE DATABASE IF NOT EXISTS tutorial CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE tutorial;
CREATE TABLE IF NOT EXISTS product (
    id BINARY(16) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price DECIMAL(10, 2) NOT NULL
);

CREATE TABLE IF NOT EXISTS orders (
    id BINARY(16) PRIMARY KEY,
    buyer_email VARCHAR(255) NOT NULL,
    total_value DECIMAL(10, 2) NOT NULL,
    order_timestamp TIMESTAMP NOT NULL
 );

CREATE TABLE IF NOT EXISTS order_product (
    order_id BINARY(16),
    product_id BINARY(16),
    PRIMARY KEY (order_id, product_id),
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (product_id) REFERENCES product(id)
);
