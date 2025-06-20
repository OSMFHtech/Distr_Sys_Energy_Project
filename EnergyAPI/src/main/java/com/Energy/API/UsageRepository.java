package com.Energy.API;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface UsageRepository extends JpaRepository<UsageEntry, LocalDateTime> {
    List<UsageEntry> findByHourBetween(LocalDateTime start, LocalDateTime end);
}