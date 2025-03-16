package dev.tutorial.productorderservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
public class ProductOrderServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(ProductOrderServiceApplication.class, args);
  }
}
