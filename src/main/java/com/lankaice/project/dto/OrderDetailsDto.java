package com.lankaice.project.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class OrderDetailsDto {
   private int orderDetailsId;
   private int orderId;
   private int productId;
   private int quantity;
   private double price;
   private String request_time;
}
