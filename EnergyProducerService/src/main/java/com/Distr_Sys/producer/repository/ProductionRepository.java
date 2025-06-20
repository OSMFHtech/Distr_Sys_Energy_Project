package com.Distr_Sys.producer.repository;

import com.Distr_Sys.producer.model.ProductionRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProductionRepository extends JpaRepository<ProductionRecord, Integer> {
    Optional<ProductionRecord> findTopByOrderByDatetimeDesc();
}