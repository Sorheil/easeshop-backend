package com.nexora.easeshop.exceptions;

public class ProductAlreadyInCartException extends IllegalStateException {
    public ProductAlreadyInCartException(String message) {
        super(message);
    }
}
