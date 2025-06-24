package com.Energy.API.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "hourly_usage")
public class UsageEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usage_hour")
    private LocalDateTime hour;

    private double communityProduced;
    private double communityUsed;
    private double gridUsed;

    public Long getId() { return id; }
    public LocalDateTime getHour() { return hour; }
    public double getCommunityProduced() { return communityProduced; }
    public double getCommunityUsed() { return communityUsed; }
    public double getGridUsed() { return gridUsed; }

    public void setId(Long id) { this.id = id; }
    public void setHour(LocalDateTime hour) { this.hour = hour; }
    public void setCommunityProduced(double communityProduced) { this.communityProduced = communityProduced; }
    public void setCommunityUsed(double communityUsed) { this.communityUsed = communityUsed; }
    public void setGridUsed(double gridUsed) { this.gridUsed = gridUsed; }
}