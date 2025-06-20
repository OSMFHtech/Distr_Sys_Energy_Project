package com.Distr_Sys.usage.repository;

import com.Distr_Sys.usage.model.HourlyUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.Optional;

public interface HourlyUsageRepository extends JpaRepository<HourlyUsage, Long> {
    Optional<HourlyUsage> findByUsageHour(LocalDateTime usageHour);
}