package com.balonek.connections.controller.transport;

/**
 * Created by kris on 10/14/15.
 */
public class ClientErrorDto {

    private final String message;
    private final String requestUri;

    public ClientErrorDto(String message, String requestUri) {
        this.message = message;
        this.requestUri = requestUri;
    }

    public String getMessage() {
        return message;
    }

    public String getRequestUri() {
        return requestUri;
    }
}
