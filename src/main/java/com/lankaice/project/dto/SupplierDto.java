package com.lankaice.project.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SupplierDto {
    private String supplierId;
    private String name;
    private String nic;
    private String contact;
    private String email;
    private String address;

    public SupplierDto(String supplierId, String name,String email) {
        this.supplierId = supplierId;
        this.name = name;
        this.email=email;
    }
    @Override
    public String toString() {
        return supplierId;
    }
}
