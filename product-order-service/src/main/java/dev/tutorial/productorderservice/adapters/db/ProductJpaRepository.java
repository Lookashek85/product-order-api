package dev.tutorial.productorderservice.adapters.db;

import dev.tutorial.productorderservice.adapters.db.model.ProductDb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductJpaRepository extends JpaRepository<ProductDb, UUID> {

  boolean existsByName(String name);
}
