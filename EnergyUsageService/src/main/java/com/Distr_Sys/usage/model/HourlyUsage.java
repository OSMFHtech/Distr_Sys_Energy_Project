package com.Distr_Sys.usage.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "hourly_usage")
public class HourlyUsage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usage_hour")
    private LocalDateTime usageHour;
    private double communityProduced;
    private double communityUsed;
    private double gridUsed;

    public HourlyUsage() {}

    public HourlyUsage(LocalDateTime usageHour) {
        this.usageHour = usageHour;
    }

    public Long getId() { return id; }
    public LocalDateTime getUsageHour() { return usageHour; }
    public void setUsageHour(LocalDateTime usageHour) { this.usageHour = usageHour; }
    public double getCommunityProduced() { return communityProduced; }
    public void setCommunityProduced(double communityProduced) { this.communityProduced = communityProduced; }
    public double getCommunityUsed() { return communityUsed; }
    public void setCommunityUsed(double communityUsed) { this.communityUsed = communityUsed; }
    public double getGridUsed() { return gridUsed; }
    public void setGridUsed(double gridUsed) { this.gridUsed = gridUsed; }
}