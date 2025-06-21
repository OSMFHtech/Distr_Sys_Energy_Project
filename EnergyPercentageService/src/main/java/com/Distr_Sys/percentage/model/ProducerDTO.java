// File: EnergyPercentageService/src/main/java/com/Distr_Sys/percentage/model/ProducerDTO.java
package com.Distr_Sys.percentage.model;

public class ProducerDTO {
    private Long id;
    private String name;
    private double producedKw;

    public ProducerDTO() {}

    public ProducerDTO(Long id, String name, double producedKw) {
        this.id = id;
        this.name = name;
        this.producedKw = producedKw;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getProducedKw() { return producedKw; }
    public void setProducedKw(double producedKw) { this.producedKw = producedKw; }
}