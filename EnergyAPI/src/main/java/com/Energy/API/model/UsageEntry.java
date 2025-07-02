package com.Energy.API.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "hourly_usage")
@Data
public class UsageEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usage_hour")
    private LocalDateTime hour;
    private double communityProduced;
    private double communityUsed;
    private double gridUsed;

    @Transient
    public double getGridUsedDisplay() {
        return Math.round((gridUsed / 100.0) * 1000.0) / 1000.0;
    }
}