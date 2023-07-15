package com.example.jwt.global;

import com.example.jwt.exception.RestException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 全局返回结果包装处理类
 */
@Slf4j

@RestControllerAdvice(basePackages = "com.example.jwt.controller")
public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {


    @Override
    public boolean supports(
            @NonNull MethodParameter returnType,
            @NonNull Class<? extends HttpMessageConverter<?>> converterType
    ) {
        // 如果已经是 RestResult 类型，则不需要包装
        if (RestResult.class.equals(returnType.getParameterType())) return false;
        // 判断是否有 @NoRestResult
        return !returnType.hasMethodAnnotation(NoRestResult.class);
    }

    @Override
    public Object beforeBodyWrite(
            Object body,
            @NonNull MethodParameter returnType,
            @NonNull MediaType selectedContentType,
            @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
            @NonNull ServerHttpRequest request,
            @NonNull ServerHttpResponse response
    ) {
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        Class<?> type = returnType.getParameterType();
        // void 类型直接返回成功
        if (Void.class.equals(type)) {
            return RestResult.success();
        }
        // String 类型单独处理
        if (String.class.equals(type)) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                return objectMapper.writeValueAsString(RestResult.success(body));
            } catch (JsonProcessingException e) {
                throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
            }
        }
        // 其他返回类型包装后返回
        return RestResult.success(body);
    }
}
