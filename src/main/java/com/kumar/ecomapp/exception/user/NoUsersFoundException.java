package com.kumar.ecomapp.exception.user;

public class NoUsersFoundException extends RuntimeException {
    public NoUsersFoundException(String message) {
        super(message);
    }
}
