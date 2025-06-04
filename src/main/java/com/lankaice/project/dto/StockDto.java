package com.lankaice.project.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class StockDto {
    private int stockId;
    private String productId;
    private String productName;
    private Integer qty;
    private String date;
    private String time;

}

