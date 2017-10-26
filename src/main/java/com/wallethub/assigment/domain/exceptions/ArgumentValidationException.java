package com.wallethub.assigment.domain.exceptions;

public class ArgumentValidationException extends RuntimeException {

    private String message;

    public ArgumentValidationException(String message) {
        super(message);
        this.message = message;
    }
}
