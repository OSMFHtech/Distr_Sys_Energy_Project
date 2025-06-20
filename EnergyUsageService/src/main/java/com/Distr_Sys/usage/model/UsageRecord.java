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

    @Enumerated(EnumType.STRING)
    private UsageType type;

    public UsageRecord() {}

    public UsageRecord(Long userId, Instant timestamp, int usedKw, UsageType type) {
        this.userId = userId;
        this.timestamp = timestamp;
        this.usedKw = usedKw;
        this.type = type;
    }

    // For backward compatibility
    public UsageRecord(Long userId, Instant timestamp, int usedKw) {
        this(userId, timestamp, usedKw, UsageType.USER);
    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
    public int getUsedKw() { return usedKw; }
    public void setUsedKw(int usedKw) { this.usedKw = usedKw; }
    public UsageType getType() { return type; }
    public void setType(UsageType type) { this.type = type; }
}