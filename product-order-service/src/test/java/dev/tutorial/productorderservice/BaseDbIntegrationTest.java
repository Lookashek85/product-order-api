package dev.tutorial.productorderservice;

import dev.tutorial.productorderservice.utils.TimestampProvider;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(classes = {ProductOrderServiceApplication.class, BaseDbIntegrationTest.TestConfig.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@Testcontainers
@ActiveProfiles("test")
public abstract class BaseDbIntegrationTest {

  @Container
  public static MySQLContainer<?> mysqlContainer =
      new MySQLContainer<>("mysql:8.0")
          .withEnv("MYSQL_TIME_ZONE", "UTC")
          .withDatabaseName("tutorial")
          .withUsername("lukas")
          .withPassword("password")
          .withReuse(true);

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

  @TestConfiguration
  static class TestConfig {
    @Bean
    @Primary
    public TimestampProvider timestampProvider() {
      return new TestTimestampProvider();
    }
  }
}
