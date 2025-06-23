package com.Energy.API.repository;

import com.Energy.API.model.PercentageEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface PercentageRepository extends JpaRepository<PercentageEntry, LocalDateTime> {
}