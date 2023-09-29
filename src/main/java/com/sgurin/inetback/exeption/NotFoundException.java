package com.sgurin.inetback.exeption;

public class NotFoundException extends RuntimeException {
    public NotFoundException(Long id) {
        super("id not found : " + id);
    }
    public NotFoundException(String object) {
        super(object);
    }
}