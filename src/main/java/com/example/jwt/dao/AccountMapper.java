package com.example.jwt.dao;

import com.example.jwt.entity.Account;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Optional;

@Mapper
public interface AccountMapper {

    @Select("select * from t_account where id = #{id} limit 1")
    Account findById(Long id);

    @Select("select * from t_account where username = #{username} limit 1")
    Optional<Account> findByUsername(String username);

    @Insert("insert into t_account(username, password, email, roles, locked, enabled) " +
            "values(#{username}, #{password}, #{email}, #{roles}, #{locked}, #{enabled})")
    void insert(Account account);

}
