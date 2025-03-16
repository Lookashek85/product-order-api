package dev.tutorial.productorderservice.adapters.db;

import dev.tutorial.productorderservice.adapters.db.model.ProductDb;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductJpaRepository extends JpaRepository<ProductDb, UUID> {}
