package com.Distr_Sys.percentage.model;

import java.time.LocalDateTime;

public class UsageDTO {
    private Long userId;
    private LocalDateTime timestamp;
    private int usedKw;

    public UsageDTO() {}
    public Long getUserId() { return userId; }
    public void setUserId(Long u) { this.userId = u; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime t) { this.timestamp = t; }
    public int getUsedKw() { return usedKw; }
    public void setUsedKw(int u) { this.usedKw = u; }
}