package com.Distr_Sys.percentage.model;

import java.time.Instant;

public class UsageDTO {
    private Long userId;
    private Instant timestamp;
    private int usedKw;

    public UsageDTO() {}
    public Long getUserId() { return userId; }
    public void setUserId(Long u) { this.userId = u; }
    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant t) { this.timestamp = t; }
    public int getUsedKw() { return usedKw; }
    public void setUsedKw(int u) { this.usedKw = u; }
}
