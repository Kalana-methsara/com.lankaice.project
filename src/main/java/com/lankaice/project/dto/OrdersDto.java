package com.lankaice.project.dto;

import com.mysql.cj.x.protobuf.MysqlxCrud;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class OrdersDto {
    private int orderId;
    private int customerId;
    private String orderDate;
    private String orderTime;
    private String status;
    private double totalAmount;

    public OrdersDto(int customerId,String status, double totalAmount) {
        this.customerId = customerId;
        this.status = status;
        this.totalAmount = totalAmount;
    }
}
