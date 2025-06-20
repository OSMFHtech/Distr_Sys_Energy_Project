package com.Distr_Sys.usage.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "hourly_usage")
public class HourlyUsage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime hour;
    private int communityProduced;
    private int communityUsed;
    private int gridUsed;

    public HourlyUsage() {}

    public HourlyUsage(LocalDateTime hour) {
        this.hour = hour;
    }

    public Long getId() { return id; }
    public LocalDateTime getHour() { return hour; }
    public void setHour(LocalDateTime hour) { this.hour = hour; }
    public int getCommunityProduced() { return communityProduced; }
    public void setCommunityProduced(int communityProduced) { this.communityProduced = communityProduced; }
    public int getCommunityUsed() { return communityUsed; }
    public void setCommunityUsed(int communityUsed) { this.communityUsed = communityUsed; }
    public int getGridUsed() { return gridUsed; }
    public void setGridUsed(int gridUsed) { this.gridUsed = gridUsed; }
}