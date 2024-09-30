package com.api.supermarketcrud.Exceptions;

public class    ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
