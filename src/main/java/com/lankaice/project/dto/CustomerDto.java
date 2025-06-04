package com.lankaice.project.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class CustomerDto {
    private String customerId;
    private String name;
    private String nic;
    private String email;
    private String contact;
    private String address;
    private String description;

    public CustomerDto(String customerId, String name) {
        this.customerId = customerId;
        this.name = name;
    }

    public CustomerDto(String id) {
        this.customerId = id;
    }

    @Override
    public String toString() {
        return customerId + " " + name;
    }
}
