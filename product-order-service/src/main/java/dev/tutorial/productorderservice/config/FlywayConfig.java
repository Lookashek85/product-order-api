package dev.tutorial.productorderservice.config;

import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.flyway.FlywayProperties;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(FlywayProperties.class)
public class FlywayConfig {

  @Bean(initMethod = "migrate")
  public Flyway flyway(
      DataSourceProperties dataSourceProperties, FlywayProperties flywayProperties) {
    return new Flyway(
        Flyway.configure()
            .baselineOnMigrate(flywayProperties.isBaselineOnMigrate())
            .dataSource(
                dataSourceProperties.getUrl(),
                dataSourceProperties.getUsername(),
                dataSourceProperties.getPassword())
            .failOnMissingLocations(flywayProperties.isFailOnMissingLocations())
            .locations(flywayProperties.getLocations().getFirst())
            .cleanDisabled(flywayProperties.isCleanDisabled()));
  }
}
