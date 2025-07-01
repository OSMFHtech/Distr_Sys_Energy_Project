package com.Energy.API.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "percentage_record")
@Data
public class PercentageEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime hour;
    private double communityDepleted;
    private double gridPortion;
}