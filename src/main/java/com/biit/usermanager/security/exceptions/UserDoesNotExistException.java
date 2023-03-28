package com.biit.usermanager.security.exceptions;

public class UserDoesNotExistException extends Exception {
    private static final long serialVersionUID = -8126258050535956599L;

    public UserDoesNotExistException(String message) {
        super(message);
    }

    public UserDoesNotExistException(String message, Throwable e) {
        super(message, e);
    }
}
