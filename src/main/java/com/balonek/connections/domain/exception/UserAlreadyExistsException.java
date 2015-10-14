package com.balonek.connections.domain.exception;

/**
 * Created by kris on 10/14/15.
 */
public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
