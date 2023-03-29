package com.biit.usermanager.security.exceptions;

public class OrganizationDoesNotExistException extends Exception {
    private static final long serialVersionUID = -8126258050535956599L;

    public OrganizationDoesNotExistException(String message) {
        super(message);
    }

    public OrganizationDoesNotExistException(String message, Throwable e) {
        super(message, e);
    }
}
