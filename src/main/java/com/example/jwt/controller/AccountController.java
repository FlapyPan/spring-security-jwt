package com.example.jwt.controller;

import com.example.jwt.entity.Account;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/account")
public class AccountController {

    /**
     * 获取当前账户信息
     */
    @GetMapping("/info")
    Account info() {
        // 从 security 上下文中获取认证信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Account) authentication.getDetails();
    }

}
