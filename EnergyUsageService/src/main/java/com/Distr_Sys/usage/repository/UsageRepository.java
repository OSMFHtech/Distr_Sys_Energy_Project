package com.Distr_Sys.usage.repository;

import com.Distr_Sys.usage.model.UsageRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsageRepository extends JpaRepository<UsageRecord, Long> {
    Optional<UsageRecord> findTopByUserIdOrderByTimestampDesc(Long userId);
}