package com.Distr_Sys.usage.model;

import java.io.Serializable;

public class UpdateMessage implements Serializable {
    private long hour;
    private double communityProduced;
    private double communityUsed;
    private double gridUsed;

    // Getters and setters
    public long getHour() { return hour; }
    public void setHour(long hour) { this.hour = hour; }
    public double getCommunityProduced() { return communityProduced; }
    public void setCommunityProduced(double communityProduced) { this.communityProduced = communityProduced; }
    public double getCommunityUsed() { return communityUsed; }
    public void setCommunityUsed(double communityUsed) { this.communityUsed = communityUsed; }
    public double getGridUsed() { return gridUsed; }
    public void setGridUsed(double gridUsed) { this.gridUsed = gridUsed; }
}