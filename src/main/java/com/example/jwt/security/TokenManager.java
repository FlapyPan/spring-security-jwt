package com.example.jwt.security;

import com.example.jwt.entity.Account;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

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
    public String generateJwtToken(Authentication authentication) {
        Claims claims = Jwts.claims();
        // 将用户信息放入 claims
        Account account = (Account) authentication.getPrincipal();
        claims.put("id", account.getId());
        claims.put("username", account.getUsername());
        claims.put("email", account.getEmail());
        claims.put("roles", account.getRoles());
        // 设置 token 生成时间
        claims.setIssuedAt(new Date());
        // 设置 token 过期时间
        claims.setExpiration(new Date(System.currentTimeMillis() + tokenValidity));
        return Jwts.builder()
                .setClaims(claims)
                .signWith(key, SignatureAlgorithm.HS256) // 签名密钥
                .compact();
    }

    /**
     * 校验 token
     */
    public boolean validateJwtToken(String token) {
        if (!StringUtils.hasText(token)) return false;
        Claims claims;
        try {
            claims = getClaimsFromToken(token);
        } catch (JwtException e) {
            return false;
        }
        // 校验过期时间
        Date now = new Date();
        return now.before(claims.getExpiration()) && now.after(claims.getIssuedAt());
    }

    /**
     * 从 token 中获取用户信息
     */
    public Account getAccountFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        Account account = new Account();
        account.setId((Integer) claims.get("id"));
        account.setUsername((String) claims.get("username"));
        account.setEmail((String) claims.get("email"));
        account.setRoles((String) claims.get("roles"));
        return account;
    }

    /**
     * 从 token 获取 Claims
     */
    private Claims getClaimsFromToken(String token) throws JwtException {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


}
