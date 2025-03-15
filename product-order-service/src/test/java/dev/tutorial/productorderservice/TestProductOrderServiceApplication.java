package dev.tutorial.productorderservice;

import org.springframework.boot.SpringApplication;

public class TestProductOrderServiceApplication {

  public static void main(String[] args) {
    SpringApplication.from(ProductOrderServiceApplication::main)
        .with(TestcontainersConfiguration.class)
        .run(args);
  }
}
