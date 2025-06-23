package com.Distr_Sys.usage.repository;

import com.Distr_Sys.usage.model.UsageRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface UsageRepository extends JpaRepository<UsageRecord, Long> {
    Optional<UsageRecord> findTopByUserIdOrderByTimestampDesc(Long userId);


    Optional<UsageRecord> findTopByOrderByTimestampDesc();

    @Query("SELECT u.type, SUM(CASE WHEN u.type = com.Distr_Sys.usage.model.UsageType.PRODUCER THEN u.producedKw ELSE u.usedKw END) FROM UsageRecord u GROUP BY u.type")
    List<Object[]> aggregateUsageByType();
}