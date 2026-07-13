package com.kumar.ecomapp.exception.product;

public class NoProductsFoundException extends RuntimeException {
    public NoProductsFoundException(String message) {
        super(message);
    }
}
