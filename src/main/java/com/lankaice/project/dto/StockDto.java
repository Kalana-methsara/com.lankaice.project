package com.lankaice.project.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class StockDto {
    private int stockId;
    private int productId;
    private int stockQuantity;
    private String lastUpdate;
}
