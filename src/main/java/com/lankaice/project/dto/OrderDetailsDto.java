package com.lankaice.project.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class OrderDetailsDto {
   private int orderId;
   private String productId;
   private int quantity;
   private double unitPrice;
   private double discount;
}
