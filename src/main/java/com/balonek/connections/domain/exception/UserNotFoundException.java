package com.balonek.connections.domain.exception;

/**
 * Created by Kris Balonek
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
