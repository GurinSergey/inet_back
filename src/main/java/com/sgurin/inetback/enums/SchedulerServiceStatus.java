package com.sgurin.inetback.enums;

public enum SchedulerServiceStatus {
    DISABLE(0, "DISABLE"),
    ACTIVE(1, "ACTIVE");

    private int index;
    private String value;

    SchedulerServiceStatus(int index, String value) {
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
