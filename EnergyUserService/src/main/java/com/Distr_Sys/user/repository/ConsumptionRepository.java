package com.Distr_Sys.user.repository;

import com.Distr_Sys.user.model.ConsumptionRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ConsumptionRepository extends JpaRepository<ConsumptionRecord, Long> {
    Optional<ConsumptionRecord> findTopByOrderByDatetimeDesc();
}
