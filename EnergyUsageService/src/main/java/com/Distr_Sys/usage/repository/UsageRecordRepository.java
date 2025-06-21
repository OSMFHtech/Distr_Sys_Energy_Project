package com.Distr_Sys.usage.repository;

import com.Distr_Sys.usage.model.UsageRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsageRecordRepository extends JpaRepository<UsageRecord, Long> {
}