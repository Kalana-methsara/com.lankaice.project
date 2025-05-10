package com.lankaice.project.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SupplierDto {
    private int supplierId;
    private String name;
    private String nic;
    private String contact;
    private String email;
    private String address;

}
