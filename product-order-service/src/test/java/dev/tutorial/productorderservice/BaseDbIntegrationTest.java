package dev.tutorial.productorderservice;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(classes = ProductOrderServiceApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Testcontainers
public abstract class BaseDbIntegrationTest {

  @Container
  public static MySQLContainer<?> mysqlContainer =
      new MySQLContainer<>("mysql:8.0")
          .withEnv("MYSQL_TIME_ZONE", "UTC")
          .withDatabaseName("tutorial")
          .withUsername("lukas")
          .withPassword("password");

  @DynamicPropertySource
  static void registerProperties(DynamicPropertyRegistry registry) {
    registry.add(
        "spring.datasource.url",
        () ->
            mysqlContainer.getJdbcUrl()
                + "?useUnicode=true"
                + "&characterEncoding=UTF-8"
                + "&serverTimezone=UTC"
                + "&useLegacyDatetimeCode=false" // Force modern datetime handling
                + "&connectionTimeZone=UTC");
    registry.add("spring.datasource.username", mysqlContainer::getUsername);
    registry.add("spring.datasource.password", mysqlContainer::getPassword);
  }
}
