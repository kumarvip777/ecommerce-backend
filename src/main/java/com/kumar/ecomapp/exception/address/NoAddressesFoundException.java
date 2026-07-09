package com.kumar.ecomapp.exception.address;

public class NoAddressesFoundException extends RuntimeException {
    public NoAddressesFoundException(String message) {
        super(message);
    }
}
