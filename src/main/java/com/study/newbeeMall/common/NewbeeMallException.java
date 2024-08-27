package com.study.newbeeMall.common;

public class NewbeeMallException extends RuntimeException {

    public NewbeeMallException() {
    }

    public NewbeeMallException(String message) {
        super(message);
    }

    /**
     * 丢出一个异常
     *
     * @param message
     */
    public static void fail(String message) {
        throw new NewbeeMallException(message);
    }

}
