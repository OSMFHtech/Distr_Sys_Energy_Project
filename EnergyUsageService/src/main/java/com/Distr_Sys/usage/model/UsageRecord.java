package com.Distr_Sys.usage.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
public class UsageRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Instant timestamp;
    private Integer usedKw; // Changed to Integer
    private Double producedKw; // New field

    @Enumerated(EnumType.STRING)
    private UsageType type; // New field

    public UsageRecord() {}

    public UsageRecord(Long userId, Instant timestamp, Integer usedKw) {
        this.userId = userId;
        this.timestamp = timestamp;
        this.usedKw = usedKw;
    }

    // Getters and setters
    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
    public Integer getUsedKw() { return usedKw; }
    public void setUsedKw(Integer usedKw) { this.usedKw = usedKw; }
    public Double getProducedKw() { return producedKw; }
    public void setProducedKw(Double producedKw) { this.producedKw = producedKw; }
    public UsageType getType() { return type; }
    public void setType(UsageType type) { this.type = type; }
}