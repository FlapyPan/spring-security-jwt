package com.example.jwt.global;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 统一返回结构
 */
@Data
@AllArgsConstructor
public class RestResult {
    private int code;
    private Object data;

    public boolean isSuccess() {
        return code == 200;
    }

    public static RestResult success() {
        return new RestResult(200, null);
    }

    public static RestResult success(Object data) {
        return new RestResult(200, data);
    }

    public static RestResult failure(int code) {
        return new RestResult(code, null);
    }

    public static RestResult failure(int code, Object data) {
        return new RestResult(code, data);
    }
}
