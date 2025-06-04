package com.lankaice.project.dto;

import com.mysql.cj.x.protobuf.MysqlxCrud;
import lombok.*;

import java.util.ArrayList;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class OrdersDto {
    private int orderId;
    private String customerId;
    private String orderDate;
    private String orderTime;
    private String description;
    private String vehicle_number;
    private double totalAmount;
    private ArrayList<OrderDetailsDto> cartList;


}
