package com.kumar.ecomapp.exception.cart;

public class NoCartItemsFoundException extends RuntimeException {
    public NoCartItemsFoundException(String message) {
        super(message);
    }
}
