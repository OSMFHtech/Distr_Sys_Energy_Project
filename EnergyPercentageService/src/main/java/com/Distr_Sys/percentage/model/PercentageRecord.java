package com.Distr_Sys.percentage.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class PercentageRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime hour;
    private double communityDepleted;
    private double gridPortion;

    public PercentageRecord() {}

    public PercentageRecord(LocalDateTime hour, double communityDepleted, double gridPortion) {
        this.hour = hour;
        this.communityDepleted = communityDepleted;
        this.gridPortion = gridPortion;
    }

    public Long getId() { return id; }
    public LocalDateTime getHour() { return hour; }
    public void setHour(LocalDateTime hour) { this.hour = hour; }
    public double getCommunityDepleted() { return communityDepleted; }
    public void setCommunityDepleted(double communityDepleted) { this.communityDepleted = communityDepleted; }
    public double getGridPortion() { return gridPortion; }
    public void setGridPortion(double gridPortion) { this.gridPortion = gridPortion; }
}