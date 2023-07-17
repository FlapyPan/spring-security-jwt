package com.example.jwt.service;

import com.example.jwt.dao.AccountMapper;
import com.example.jwt.entity.Account;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 账号相关服务
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class AccountService implements UserDetailsService {

    private final AccountMapper mapper;

    @Override
    public Account loadUserByUsername(String username) throws UsernameNotFoundException {
        return mapper.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("用户名不存在"));
    }

    public Account findById(Long id) {
        if (id == null || id <=0) return null;
        return mapper.findById(id);
    }

}
