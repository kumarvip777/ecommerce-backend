package com.kumar.ecomapp.exception.address;

public class AddressLimitExceededException extends RuntimeException {
    public AddressLimitExceededException(String message) {
        super(message);
    }
}
