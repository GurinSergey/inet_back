package com.sgurin.inetback.exeption;

public class InvalidTokenException extends RuntimeException{
    public InvalidTokenException() {
        super("Token is invalid");
    }
}
