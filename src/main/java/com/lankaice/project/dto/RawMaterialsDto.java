package com.lankaice.project.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class RawMaterialsDto {
    private int rawMaterialId;
    private String name;
    private String unitType;
    private double unitCost;
    private int quantityAvailable;
}
