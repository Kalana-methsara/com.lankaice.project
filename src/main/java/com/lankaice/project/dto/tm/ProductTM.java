package com.lankaice.project.dto.tm;

import javafx.beans.property.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ProductTM {
        private String productId; // new field
        private String productName;
        private double price;
        private int qty;
        private double discount;

}
