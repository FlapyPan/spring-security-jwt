package com.example.jwt.controller;

import com.example.jwt.entity.Account;
import com.example.jwt.vo.LoginRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Value("${jwt.token-validity}")
    private long tokenValidity;
    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;

    /**
     * 登录获取 token
     */
    @PostMapping("/login")
    public String login(@RequestBody @Validated LoginRequest loginRequest) {
        // 注入认证管理器来认证用户名和密码
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(), loginRequest.getPassword()));
        // 生成 token 返回
        return generateJwtToken((Account) authentication.getPrincipal());
    }


    /**
     * 生成 JWT Token
     */
    private String generateJwtToken(Account account) {
        Instant now = Instant.now();
        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                // 设置 token 生成时间
                .issuedAt(now)
                // 设置 token 过期时间
                .expiresAt(now.plusMillis(tokenValidity))
                .subject(account.getUsername())
                .claims(map -> {
                    // 将用户信息放入 claims
                    map.put("id", account.getId());
                    map.put("username", account.getUsername());
                    map.put("email", account.getEmail());
                    map.put("roles", account.getRoles());
                }).build();
        // 生成 token
        return jwtEncoder.encode(JwtEncoderParameters.from(claimsSet))
                .getTokenValue();
    }

}
