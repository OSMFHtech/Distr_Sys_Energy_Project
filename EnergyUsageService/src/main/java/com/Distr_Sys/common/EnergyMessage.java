package com.Distr_Sys.common;

import java.time.Instant;

public class EnergyMessage {
    public enum Type { USER, PRODUCER }

    private Type type;
    private Long userId;
    private Instant datetime;
    private double kwh;

    public EnergyMessage() {}

    public EnergyMessage(Type type, Long userId, Instant datetime, double kwh) {
        this.type = type;
        this.userId = userId;
        this.datetime = datetime;
        this.kwh = kwh;
    }

    public Type getType() { return type; }
    public void setType(Type type) { this.type = type; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Instant getDatetime() { return datetime; }
    public void setDatetime(Instant datetime) { this.datetime = datetime; }

    public double getKwh() { return kwh; }
    public void setKwh(double kwh) { this.kwh = kwh; }
}