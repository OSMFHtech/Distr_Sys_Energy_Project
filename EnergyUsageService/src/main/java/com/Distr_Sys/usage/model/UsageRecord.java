package com.Distr_Sys.usage.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "usage_records")
public class UsageRecord {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Instant timestamp;
    private int usedKw;

    public UsageRecord() {}
    public UsageRecord(Long userId, Instant timestamp, int usedKw) {
        this.userId = userId;
        this.timestamp = timestamp;
        this.usedKw = usedKw;
    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
    public int getUsedKw() { return usedKw; }
    public void setUsedKw(int usedKw) { this.usedKw = usedKw; }
}
