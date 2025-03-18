package dev.tutorial.productorderservice.adapters.db;

import dev.tutorial.productorderservice.adapters.db.model.OrderDb;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrdersJpaRepository extends JpaRepository<OrderDb, UUID> {

  @Query("SELECT o FROM OrderDb o WHERE o.orderTimestamp BETWEEN :from AND :to")
  List<OrderDb> findAllWithTimeRange(@Param("from") Timestamp from, @Param("to") Timestamp to);
}
