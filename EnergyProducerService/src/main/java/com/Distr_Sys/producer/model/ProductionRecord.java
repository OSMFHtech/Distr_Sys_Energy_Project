package com.Distr_Sys.producer.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "production_record")
public class ProductionRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "producer_id")
    private Integer producerId;

    private Double kwh;

    private LocalDateTime datetime;

    public ProductionRecord() {}

    public ProductionRecord(Integer producerId, Double kwh, LocalDateTime datetime) {
        this.producerId = producerId;
        this.kwh = kwh;
        this.datetime = datetime;
    }

    public Integer getId() { return id; }
    public Integer getProducerId() { return producerId; }
    public void setProducerId(Integer producerId) { this.producerId = producerId; }
    public Double getKwh() { return kwh; }
    public void setKwh(Double kwh) { this.kwh = kwh; }
    public LocalDateTime getDatetime() { return datetime; }
    public void setDatetime(LocalDateTime datetime) { this.datetime = datetime; }
}