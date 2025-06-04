package com.lankaice.project.dto.tm;

import javafx.scene.control.Button;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CartItemTM {
    private int cartId;
    private String supplierId;
    private int materialId;
    private String name;
    private String unitType;
    private double unitPrice;
    private int quantity;
    private double total;

    public CartItemTM(String supplierId, int materialId, String name, String unitType, double unitPrice, int qty, double total) {
        this.supplierId = supplierId;
        this.materialId = materialId;
        this.name = name;
        this.unitType = unitType;
        this.unitPrice = unitPrice;
        this.quantity = qty;
        this.total = total;
    }
}
