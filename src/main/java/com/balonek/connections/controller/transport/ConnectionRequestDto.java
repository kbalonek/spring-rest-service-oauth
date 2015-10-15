package com.balonek.connections.controller.transport;

import org.hibernate.validator.constraints.NotEmpty;

public class ConnectionRequestDto {

    @NotEmpty
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
