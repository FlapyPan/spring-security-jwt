package com.example.jwt.controller;

import com.example.jwt.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/account")
public class AccountController {

    private final AccountService accountService;

    /**
     * 获取当前账户信息
     */
    @GetMapping("/info")
    Object info() {
        // 从 security 上下文中获取认证信息
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long id = (Long) jwt.getClaims().get("id");
        // 查询 账户
        return accountService.findById(id);
    }

}
