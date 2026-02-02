package com.mihai.library.service.exceptions;

public final class ItemAlreadyLoanedException extends RuntimeException {
    public ItemAlreadyLoanedException(String message) {
        super(message);
    }
}
