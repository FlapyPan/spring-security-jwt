package com.example.jwt;

import com.example.jwt.dao.AccountMapper;
import com.example.jwt.entity.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class SimpleMusicBackendApplicationTests {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AccountMapper accountMapper;

    @Test
    void addDefaultUser() {
        Account account = new Account();
        account.setUsername("admin");
        account.setPassword(passwordEncoder.encode("admin"));
        account.setRoles("USER,ADMIN");
        account.setEnabled(true);
        accountMapper.insert(account);
    }

    @Test
    void contextLoads() {
    }

}
