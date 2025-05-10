package com.lankaice.project.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PurchaseDto {
    private int purchaseId;
    private int supplierId;
    private int materialId;
    private String purchaseDate;
    private int quantity;
}
