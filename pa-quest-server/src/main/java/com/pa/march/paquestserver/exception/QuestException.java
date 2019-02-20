package com.pa.march.paquestserver.exception;

public class QuestException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private int code;
    private String message;

    public QuestException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

}