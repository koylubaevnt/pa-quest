package com.pa.march.paquestserver.exception;

public class QuestException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private int code;

    public QuestException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}