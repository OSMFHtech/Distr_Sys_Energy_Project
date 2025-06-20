package com.Energy.API;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class UsageEntry {
    @Id
    private LocalDateTime hour;

    private double communityProduced;
    private double communityUsed;
    private double gridUsed;

    public LocalDateTime getHour() { return hour; }
    public double getCommunityProduced() { return communityProduced; }
    public double getCommunityUsed() { return communityUsed; }
    public double getGridUsed() { return gridUsed; }

    public void setHour(LocalDateTime hour) { this.hour = hour; }
    public void setCommunityProduced(double communityProduced) { this.communityProduced = communityProduced; }
    public void setCommunityUsed(double communityUsed) { this.communityUsed = communityUsed; }
    public void setGridUsed(double gridUsed) { this.gridUsed = gridUsed; }
}