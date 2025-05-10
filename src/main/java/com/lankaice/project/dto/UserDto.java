package com.lankaice.project.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserDto {
    private String username;
    private String password;
    private String name;
    private String email;
    private String role;
}
