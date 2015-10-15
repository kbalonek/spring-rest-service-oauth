package com.balonek.connections.controller.transport;

import java.util.Collection;

public class UserWithConnectionsDto {

    private final String userId;
    private final String username;
    private final Collection<String> connections;

    public UserWithConnectionsDto(String userId, String username, Collection<String> connections) {

        this.userId = userId;
        this.username = username;
        this.connections = connections;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public Collection<String> getConnections() {
        return connections;
    }
}
