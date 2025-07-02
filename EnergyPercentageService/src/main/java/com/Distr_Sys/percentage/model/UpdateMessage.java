package com.Distr_Sys.percentage.model;

public class UpdateMessage {
    private String hour;
    private double communityProduced;
    private double communityUsed;
    private double gridUsed;
    private double gridUsedDisplay; // add this

    public UpdateMessage() {}

    public String getHour() { return hour; }
    public void setHour(String hour) { this.hour = hour; }

    public double getCommunityProduced() { return communityProduced; }
    public void setCommunityProduced(double communityProduced) { this.communityProduced = communityProduced; }

    public double getCommunityUsed() { return communityUsed; }
    public void setCommunityUsed(double communityUsed) { this.communityUsed = communityUsed; }

    public double getGridUsed() { return gridUsed; }
    public void setGridUsed(double gridUsed) { this.gridUsed = gridUsed; }

    public double getGridUsedDisplay() { return gridUsedDisplay; }
    public void setGridUsedDisplay(double gridUsedDisplay) { this.gridUsedDisplay = gridUsedDisplay; }
}