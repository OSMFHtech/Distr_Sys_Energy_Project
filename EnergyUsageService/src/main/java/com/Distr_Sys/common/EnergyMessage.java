package com.Distr_Sys.common;

import java.time.Instant;

public class EnergyMessage {
    public enum Type { USER, PRODUCER }

    private Type type;
    private Long userId;
    private Instant timestamp;
    private double kwh;

    public EnergyMessage() {}

    public EnergyMessage(Type type, Long userId, Instant timestamp, double kwh) {
        this.type = type;
        this.userId = userId;
        this.timestamp = timestamp;
        this.kwh = kwh;
    }

    public Type getType() { return type; }
    public Long getUserId() { return userId; }
    public Instant getTimestamp() { return timestamp; }
    public double getKwh() { return kwh; }
}