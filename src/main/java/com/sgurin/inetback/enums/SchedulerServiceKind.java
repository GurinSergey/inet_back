package com.sgurin.inetback.enums;

public enum SchedulerServiceKind {
    PERIODIC(0, "PERIODIC"),
    SINGLE(1, "SINGLE");

    private int index;
    private String value;

    SchedulerServiceKind(int index, String value) {
        this.index = index;
        this.value = value;
    }

    public int getIndex() {
        return index;
    }

    public String getValue() {
        return value;
    }
}