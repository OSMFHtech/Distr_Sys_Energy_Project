package com.Distr_Sys.usage.repository;

import com.Distr_Sys.usage.model.UsageRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.Optional;

public interface UsageRecordRepository extends JpaRepository<UsageRecord, Long> {
    Optional<UsageRecord> findByHour(LocalDateTime hour);
}