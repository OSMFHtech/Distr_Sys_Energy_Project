package com.Distr_Sys.usage.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "hourly_usage", uniqueConstraints = @UniqueConstraint(columnNames = "usage_hour"))
public class UsageRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usage_hour", nullable = false, unique = true)

    private LocalDateTime hour;
    private double communityProduced; // Gesamtproduktion der Community in kWh
    private double communityUsed;     // Gesamtverbrauch der Community in kWh
    private double gridUsed;          // Aus dem Netz bezogene Energie in kWh

    public UsageRecord() {}

    public UsageRecord(LocalDateTime hour, double communityProduced, double communityUsed, double gridUsed) {
        this.hour = hour;
        this.communityProduced = communityProduced;
        this.communityUsed = communityUsed;
        this.gridUsed = gridUsed;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getHour() { return hour; }
    public void setHour(LocalDateTime hour) { this.hour = hour; }

    public double getCommunityProduced() { return communityProduced; }
    public void setCommunityProduced(double communityProduced) { this.communityProduced = communityProduced; }

    public double getCommunityUsed() { return communityUsed; }
    public void setCommunityUsed(double communityUsed) { this.communityUsed = communityUsed; }

    public double getGridUsed() { return gridUsed; }
    public void setGridUsed(double gridUsed) { this.gridUsed = gridUsed; }
}