package com.Distr_Sys.percentage.model;

import java.time.Instant;

public class ProducerDTO {
    private Instant timestamp;
    private int producedKw;

    public ProducerDTO() {}
    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant t) { this.timestamp = t; }
    public int getProducedKw() { return producedKw; }
    public void setProducedKw(int p) { this.producedKw = p; }
}
