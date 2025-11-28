package com.example.laundry.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.math.BigDecimal;

/**
 * Abstract base for clothes that can be ironed.
 * Subclasses (Shirt, Pant, etc.) supply default ironing cost and type.
 *
 * Polymorphic JSON uses property "type".
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Shirt.class, name = "SHIRT"),
        @JsonSubTypes.Type(value = Pant.class, name = "PANT")
})
public abstract class Clothes {
    private String id;
    private Double weightKg;      // weight of a single item (optional)
    private BigDecimal ironCost;  // per-item ironing cost

    protected Clothes() {}

    protected Clothes(String id, Double weightKg, BigDecimal ironCost) {
        this.id = id;
        this.weightKg = weightKg;
        this.ironCost = ironCost;
    }

    // getters / setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Double getWeightKg() { return weightKg; }
    public void setWeightKg(Double weightKg) { this.weightKg = weightKg; }

    public BigDecimal getIronCost() { return ironCost; }
    public void setIronCost(BigDecimal ironCost) { this.ironCost = ironCost; }
}