package com.sgurin.inetback.exeption;

public class ValidateException extends RuntimeException {
    public ValidateException(String object) {
        super(object);
    }
}