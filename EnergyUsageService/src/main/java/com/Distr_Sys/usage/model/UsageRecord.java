package com.Distr_Sys.usage.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class UsageRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private LocalDateTime timestamp;
    private Double usedKw;
    private Double producedKw;

    @Enumerated(EnumType.STRING)
    private UsageType type;

    public UsageRecord() {}

    public UsageRecord(Long userId, LocalDateTime timestamp, Double usedKw) {
        this.userId = userId;
        this.timestamp = timestamp;
        this.usedKw = usedKw;
    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public Double getUsedKw() { return usedKw; }
    public void setUsedKw(Double usedKw) { this.usedKw = usedKw; }
    public Double getProducedKw() { return producedKw; }
    public void setProducedKw(Double producedKw) { this.producedKw = producedKw; }
    public UsageType getType() { return type; }
    public void setType(UsageType type) { this.type = type; }
}