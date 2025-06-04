package com.lankaice.project.dto;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class RawMaterialsDto {
    private int materialId;
    private String supplierId;
    private String name;
    private String unitType;
    private double unitCost;
    private String lastUpdate;
    private int quantityAvailable;

    public RawMaterialsDto(int materialId, String name, String unitType, double unitCost, int quantityAvailable) {
        this.materialId = materialId;
        this.name = name;
        this.unitType = unitType;
        this.unitCost=unitCost;
        this.quantityAvailable=quantityAvailable;
    }

    @Override
    public String toString() {
        return String.format("ID: %3s,   Name: '%s'", materialId, name);
    }

}