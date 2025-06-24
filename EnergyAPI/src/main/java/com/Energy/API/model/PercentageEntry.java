package com.Energy.API.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "percentage_record")
public class PercentageEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime hour;
    private double communityDepleted;
    private double gridPortion;

    public Long getId() { return id; }
    public LocalDateTime getHour() { return hour; }
    public double getCommunityDepleted() { return communityDepleted; }
    public double getGridPortion() { return gridPortion; }

    public void setId(Long id) { this.id = id; }
    public void setHour(LocalDateTime hour) { this.hour = hour; }
    public void setCommunityDepleted(double communityDepleted) { this.communityDepleted = communityDepleted; }
    public void setGridPortion(double gridPortion) { this.gridPortion = gridPortion; }
}