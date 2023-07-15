package com.example.jwt.security;

import com.example.jwt.global.RestResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Spring Security 配置类
 */
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // 关闭 csrf 保护
                .csrf().disable()
                // 开启跨域
                .cors().and()
                // 关闭 session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                // 放行请求
                .antMatchers("/api/auth/**").permitAll()
                .anyRequest().authenticated().and()
                // 添加 Jwt 认证过滤器
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                // 认证失败的处理
                .authenticationEntryPoint((request, response, exception) ->
                        writeFailureResponse(response, HttpStatus.UNAUTHORIZED, "请登录"))
                // 授权失败的处理
                .accessDeniedHandler((request, response, exception) ->
                        writeFailureResponse(response, HttpStatus.FORBIDDEN, exception.getMessage()))
                .and()
                .build();
    }

    /**
     * 将错误信息写入响应体
     */
    private void writeFailureResponse(HttpServletResponse response, HttpStatus httpStatus, String message) throws IOException {
        // 设置 http 状态码
        response.setStatus(httpStatus.value());
        // 设置 utf8 编码
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        // 类型 json
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ObjectMapper objectMapper = new ObjectMapper();
        RestResult result = RestResult.failure(httpStatus.value(), message);
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 提供认证管理器的 Bean
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}
