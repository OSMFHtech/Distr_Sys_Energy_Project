package com.Distr_Sys.percentage.dto;

import java.time.LocalDateTime;

public class HourlyUsage {
    private LocalDateTime usageHour;
    private int communityProduced;
    private int communityUsed;
    private int gridUsed;

    // getters and setters
    public LocalDateTime getUsageHour() {
        return usageHour;
    }
    public void setUsageHour(LocalDateTime usageHour) {
        this.usageHour = usageHour;
    }
    public int getCommunityProduced() {
        return communityProduced;
    }
    public void setCommunityProduced(int communityProduced) {
        this.communityProduced = communityProduced;
    }
    public int getCommunityUsed() {
        return communityUsed;
    }
    public void setCommunityUsed(int communityUsed) {
        this.communityUsed = communityUsed;
    }
    public int getGridUsed() {
        return gridUsed;
    }
    public void setGridUsed(int gridUsed) {
        this.gridUsed = gridUsed;
    }
}