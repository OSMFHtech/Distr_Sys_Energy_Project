
package com.Distr_Sys.common;

import java.time.Instant;

public class EnergyMessage {
    public enum Type { USER, PRODUCER }
    private Type type;
    private String association;
    private double kwh;
    private Instant timestamp;

    public EnergyMessage() {}
    public EnergyMessage(Type type, String association, double kwh, Instant timestamp) {
        this.type = type;
        this.association = association;
        this.kwh = kwh;
        this.timestamp = timestamp;
    }
    // getters and setters
}