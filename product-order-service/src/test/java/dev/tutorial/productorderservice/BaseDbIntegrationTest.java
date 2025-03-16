package dev.tutorial.productorderservice;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(classes = ProductOrderServiceApplication.class)
@Testcontainers
public abstract class BaseDbIntegrationTest {

  @Container
  public static MySQLContainer<?> mysqlContainer =
      new MySQLContainer<>("mysql:8.0")
          .withDatabaseName("tutorial")
          .withUsername("lukas")
          .withPassword("password");

  @DynamicPropertySource
  static void registerProperties(DynamicPropertyRegistry registry) {
    registry.add(
        "spring.datasource.url",
        () ->
            mysqlContainer.getJdbcUrl()
                + "?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC");
    registry.add("spring.datasource.username", mysqlContainer::getUsername);
    registry.add("spring.datasource.password", mysqlContainer::getPassword);
    registry.add("spring.flyway.enabled", () -> true);
    registry.add("spring.flyway.clean-disabled", () -> false);

    registry.add("spring.datasource.hikari.connection-timeout", () -> "60000"); // 1 minute
    registry.add("spring.datasource.hikari.maximum-pool-size", () -> "10");
    registry.add("spring.datasource.hikari.minimum-idle", () -> "5");
    registry.add("spring.datasource.hikari.idle-timeout", () -> "300000"); // 5 minutes
    registry.add("spring.datasource.hikari.max-lifetime", () -> "900000"); // 15 minutes
    registry.add("spring.datasource.hikari.validationQuery", () -> "SELECT 1");
    registry.add("spring.datasource.hikari.validationTimeout", () -> "3000"); //
  }
}
