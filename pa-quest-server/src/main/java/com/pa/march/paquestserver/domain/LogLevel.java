package com.pa.march.paquestserver.domain;

import com.fasterxml.jackson.annotation.JsonValue;

public enum LogLevel {
    ALL(0),
    DEBUG(1),
    INFO(2),
    WARN(3),
    ERROR(4),
    FATAL(5),
    OFF(6),
    UNKNOWN(-1);

    @JsonValue
    private int value;

    LogLevel(int value) {
        value = value;
    }

    public static LogLevel fromValue(int value) {
        for (LogLevel logLevel : LogLevel.values()) {
            if (logLevel.value == value) {
                return logLevel;
            }
        }
        return LogLevel.UNKNOWN;
    }
}
