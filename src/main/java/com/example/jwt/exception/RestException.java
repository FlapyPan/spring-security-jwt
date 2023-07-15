package com.example.jwt.exception;

/**
 * 自定义业务异常
 */
public class RestException extends RuntimeException {
    final int code;

    public RestException(int code, String message) {
        super(message);
        this.code = code;
    }

    public RestException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public RestException(int code, Throwable cause) {
        super(cause);
        this.code = code;
    }
}
