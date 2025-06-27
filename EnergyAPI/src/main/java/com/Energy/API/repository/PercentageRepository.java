package com.Energy.API.repository;

import com.Energy.API.model.PercentageEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PercentageRepository extends JpaRepository<PercentageEntry, Long> {}