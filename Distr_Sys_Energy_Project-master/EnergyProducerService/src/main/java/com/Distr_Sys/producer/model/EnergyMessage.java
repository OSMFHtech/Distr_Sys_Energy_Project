package com.Distr_Sys.producer.model;

import java.time.LocalDateTime;

public class EnergyMessage {
    public enum Type { PRODUCER, USER } // Type-Enum unterscheidet zwischen Produktion und Verbrauch

    private Type type;
    private Long association;
    private LocalDateTime datetime;
    private double kwh;

    public EnergyMessage() {}

    public EnergyMessage(Type type, Long association, LocalDateTime datetime, double kwh) {
        this.type = type;
        this.association = association;
        this.datetime = datetime;
        this.kwh = kwh;
    }

    public Type getType() { return type; }
    public void setType(Type type) { this.type = type; }

    public Long getAssociation() { return association; }
    public void setAssociation(Long association) { this.association = association; }

    public LocalDateTime getDatetime() { return datetime; }
    public void setDatetime(LocalDateTime datetime) { this.datetime = datetime; }

    public double getKwh() { return kwh; }
    public void setKwh(double kwh) { this.kwh = kwh; }
}