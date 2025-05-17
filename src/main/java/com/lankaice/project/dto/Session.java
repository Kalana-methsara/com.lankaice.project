package com.lankaice.project.dto;

import lombok.Getter;
import lombok.Setter;

public class Session {
    @Getter
    @Setter
    private static UserDto currentUser;
}
