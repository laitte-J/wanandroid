package com.arch.base.core.model;

public  class ResponeThrowable extends Exception {
    public int code;
    public String message;

    public ResponeThrowable(Throwable throwable, int code) {
        super(throwable);
        this.code = code;

    }

    public ResponeThrowable(Throwable throwable, String message, int code) {
        super(throwable);
        this.message = message;
        this.code = code;

    }

    public ResponeThrowable(String message, int code) {
        this.message = message;
        this.code = code;

    }
}
