package com.kumar.ecomapp.exception.order;

public class NoOrdersFoundException extends RuntimeException {
    public NoOrdersFoundException(String message) {
        super(message);
    }
}
