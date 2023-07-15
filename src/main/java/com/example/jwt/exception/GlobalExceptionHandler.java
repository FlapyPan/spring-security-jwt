package com.example.jwt.exception;

import com.example.jwt.global.RestResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * 全局异常处理类
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Spring Security 认证异常处理
     */
    @ExceptionHandler(value = AuthenticationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestResult handleAuthenticationException(AuthenticationException e) {
        String message = e.getMessage();
        log.warn("认证错误 {}", message);
        return RestResult.failure(HttpStatus.BAD_REQUEST.value(), message);
    }

    /**
     * Spring Validation 异常处理
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestResult handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        String message = e.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(","));
        log.warn("字段错误 {}", message);
        return RestResult.failure(HttpStatus.BAD_REQUEST.value(), message);
    }

    /**
     * 业务异常处理
     */
    @ExceptionHandler(value = RestException.class)
    @ResponseStatus(HttpStatus.OK)
    public RestResult handleRestException(RestException e) {
        log.error("业务错误", e);
        return RestResult.failure(e.code, e.getMessage());
    }

    /**
     * 其他异常处理
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestResult handleException(Exception e) {
        log.error("服务器内部错误：", e);
        return RestResult.failure(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
    }
}
