package com.Distr_Sys.producer.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "production_record")
public class ProductionRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "producer_id")
    private Long producerId;

    private Double kwh;

    private LocalDateTime datetime;

    public ProductionRecord() {}

    public ProductionRecord(Long producerId, Double kwh, LocalDateTime datetime) {
        this.producerId = producerId;
        this.kwh = kwh;
        this.datetime = datetime;
    }

    public Long getId() { return id; }
    public Long getProducerId() { return producerId; }
    public void setProducerId(Long producerId) { this.producerId = producerId; }
    public Double getKwh() { return kwh; }
    public void setKwh(Double kwh) { this.kwh = kwh; }
    public LocalDateTime getDatetime() { return datetime; }
    public void setDatetime(LocalDateTime datetime) { this.datetime = datetime; }
}