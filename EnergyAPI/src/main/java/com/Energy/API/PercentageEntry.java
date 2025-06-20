package com.Energy.API;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class PercentageEntry {
    @Id
    private LocalDateTime hour;

    private double communityDepleted;
    private double gridPortion;

    public LocalDateTime getHour() { return hour; }
    public double getCommunityDepleted() { return communityDepleted; }
    public double getGridPortion() { return gridPortion; }

    public void setHour(LocalDateTime hour) { this.hour = hour; }
    public void setCommunityDepleted(double communityDepleted) { this.communityDepleted = communityDepleted; }
    public void setGridPortion(double gridPortion) { this.gridPortion = gridPortion; }
}