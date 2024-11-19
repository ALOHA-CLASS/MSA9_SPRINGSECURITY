package com.aloha.security5.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.aloha.security5.domain.Users;

@Mapper
public interface UserMapper {

    // 사용자 인증(로그인) - id
    public Users login(String username);

}