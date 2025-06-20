package com.Energy.API;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface PercentageRepository extends JpaRepository<PercentageEntry, LocalDateTime> {
}