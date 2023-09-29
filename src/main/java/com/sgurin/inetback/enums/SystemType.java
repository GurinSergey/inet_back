package com.sgurin.inetback.enums;

public enum SystemType {
    NONE(0, ""),
    INET(1, "inet");

    private int value;
    private String code;

    SystemType(int value, String code) {
        this.value = value;
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}