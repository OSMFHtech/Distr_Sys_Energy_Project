package com.Distr_Sys.percentage.repository;

import com.Distr_Sys.percentage.model.PercentageRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Optional;

public interface PercentageRecordRepository extends JpaRepository<PercentageRecord, Long> {
    Optional<PercentageRecord> findByHour(Instant hour);
}