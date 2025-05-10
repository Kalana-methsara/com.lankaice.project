package com.lankaice.project.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CustomerDto {
    private int customerId;
    private String name;
    private String nic;
    private String email;
    private String contact;
    private String address;
}
