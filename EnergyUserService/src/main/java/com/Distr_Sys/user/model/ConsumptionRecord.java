package com.Distr_Sys.user.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "consumption_record")
public class ConsumptionRecord {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id")
    private Long userId;
    private Double kwh;
    private LocalDateTime datetime;

    public ConsumptionRecord() {}
    public ConsumptionRecord(Long userId, Double kwh, LocalDateTime datetime) {
        this.userId = userId;
        this.kwh = kwh;
        this.datetime = datetime;
    }
    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Double getKwh() { return kwh; }
    public void setKwh(Double kwh) { this.kwh = kwh; }
    public LocalDateTime getDatetime() { return datetime; }
    public void setDatetime(LocalDateTime datetime) { this.datetime = datetime; }
}
//mvn exec:java -Dexec.mainClass=com.Distr_Sys.EnergyUserService.EnergyUserServiceApplication