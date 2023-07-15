package com.example.jwt.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.Objects;

/**
 * Jwt token 管理器
 */
@Component
public class TokenManager {
    private final long tokenValidity;

    private final SecretKey key;

    @Autowired
    public TokenManager(
            @Value("${jwt.secret}") String jwtSecret,
            @Value("${jwt.token-validity}") long tokenValidity
    ) {
        // 根据密钥生成 key
        key = new SecretKeySpec(jwtSecret.getBytes(), SignatureAlgorithm.HS256.getJcaName());
        this.tokenValidity = tokenValidity;
    }

    /**
     * 生成 JWT Token
     */
    public String generateJwtToken(String username) {
        return Jwts.builder()
                .setSubject(username) // 用户名存放在 subject
                .setIssuedAt(new Date()) // 设置 token 生成时间
                .setExpiration(new Date(System.currentTimeMillis() + tokenValidity)) // 设置 token 过期时间
                .signWith(key, SignatureAlgorithm.HS256) // 签名密钥
                .compact();
    }

    /**
     * 校验 token
     */
    public boolean validateJwtToken(String token, UserDetails userDetails) {
        if (!StringUtils.hasText(token) || userDetails == null) return false;
        Claims claims = getClaimsFromToken(token);
        // 校验过期时间
        boolean tokenExpired = claims.getExpiration().before(new Date());
        if (tokenExpired) return false;
        // 检查用户名
        String username = claims.getSubject();
        return Objects.equals(username, userDetails.getUsername());
    }

    /**
     * 从 token 获取 Claims
     */
    private Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    /**
     * 从 token 获取用户名
     */
    public String getUsernameFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

}
