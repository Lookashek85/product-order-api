package dev.tutorial.productorderservice.adapters.db;

import java.nio.ByteBuffer;
import java.util.UUID;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements ApplicationRunner {

  private final JdbcTemplate jdbcTemplate;

  public DataInitializer(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public void run(ApplicationArguments args) throws Exception {
    Integer count =
        jdbcTemplate.queryForObject("SELECT COUNT(*) FROM tutorial.product", Integer.class);
    if (count == null || count == 0) {
      UUID milkId = UUID.fromString("a1b2c3d4-e5f6-7890-1234-567890abcdef");
      UUID breadId = UUID.fromString("b2c3d4e5-f678-9012-3456-7890abcdefa1");
      UUID eggsId = UUID.fromString("c3d4e5f6-7890-1234-5678-90abcdefa1b2");
      UUID tShirtId = UUID.fromString("d4e5f678-9012-3456-7890-abcdefa1b2c3");
      UUID sodaId = UUID.fromString("e5f67890-1234-5678-90ab-cdefa1b2c3d4");

      String sql = "INSERT INTO tutorial.product (id, name, price) VALUES (?, ?, ?)";
      try {
        jdbcTemplate.update(sql, convertUUIDToBytes(milkId), "Milk", 15.99);
        jdbcTemplate.update(sql, convertUUIDToBytes(breadId), "Bread", 10.49);
        jdbcTemplate.update(sql, convertUUIDToBytes(eggsId), "Eggs", 12.99);
        jdbcTemplate.update(sql, convertUUIDToBytes(tShirtId), "T-Shirt", 49.99);
        jdbcTemplate.update(sql, convertUUIDToBytes(sodaId), "Soda", 99.99);
      } catch (DuplicateKeyException e) {
        System.out.println("Data already exists. Skipping initialization.");
      }
    }
  }

  private byte[] convertUUIDToBytes(UUID uuid) {
    ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
    bb.putLong(uuid.getMostSignificantBits());
    bb.putLong(uuid.getLeastSignificantBits());
    return bb.array();
  }
}
