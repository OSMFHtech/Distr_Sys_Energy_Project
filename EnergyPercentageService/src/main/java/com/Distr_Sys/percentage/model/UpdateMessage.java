package com.Distr_Sys.percentage.model;

import java.time.Instant;

public class UpdateMessage {
    private Instant hour;
    private double communityProduced;
    private double communityUsed;
    private double gridUsed;

    public Instant getHour() { return hour; }
    public void setHour(Instant hour) { this.hour = hour; }
    public double getCommunityProduced() { return communityProduced; }
    public void setCommunityProduced(double communityProduced) { this.communityProduced = communityProduced; }
    public double getCommunityUsed() { return communityUsed; }
    public void setCommunityUsed(double communityUsed) { this.communityUsed = communityUsed; }
    public double getGridUsed() { return gridUsed; }
    public void setGridUsed(double gridUsed) { this.gridUsed = gridUsed; }
}