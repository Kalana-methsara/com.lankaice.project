package com.lankaice.project.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ProductDto {
    private int productId;
    private String productName;
    private double productWeight;
    private double productPricePerUnit;
}
