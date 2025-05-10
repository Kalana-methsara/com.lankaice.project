package com.lankaice.project.util;

import com.lankaice.project.dto.UserDto;

import javax.mail.Authenticator;
import java.util.Properties;

public class Session {
    private static UserDto currentUser;

    public static void setCurrentUser(UserDto user) {
        currentUser = user;
    }

    public static UserDto getCurrentUser() {
        return currentUser;
    }

}

