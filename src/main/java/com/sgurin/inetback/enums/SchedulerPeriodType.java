package com.sgurin.inetback.enums;

import java.util.Calendar;

public enum SchedulerPeriodType {
    MILLISECOND(0, Calendar.MILLISECOND),
    SECOND(1, Calendar.SECOND),
    MINUTE(2, Calendar.MINUTE),
    HOUR(3, Calendar.HOUR),
    DATE(4, Calendar.DATE);

    private int index;
    private int value;

    SchedulerPeriodType(int index, int value) {
        this.index = index;
        this.value = value;
    }

    public int getIndex() {
        return index;
    }

    public int getValue() {
        return value;
    }
}