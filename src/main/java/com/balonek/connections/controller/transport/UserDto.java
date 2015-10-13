package com.balonek.connections.controller.transport;

public class UserDto {

    private final String userId;
    private final String username;

    public UserDto(String userId, String username) {

        this.userId = userId;
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }
}
