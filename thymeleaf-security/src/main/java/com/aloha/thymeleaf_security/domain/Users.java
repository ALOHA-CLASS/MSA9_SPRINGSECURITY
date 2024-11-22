package com.aloha.thymeleaf_security.domain;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.type.Alias;

import lombok.Data;

@Data
@Alias("Users")
public class Users {
    private Long no;
    private String username;
    private String password;
    private String name;
    private String email;
    private Date createdAt;
    private Date updatedAt;
    private int enabled;

    private List<UserAuth> authList;

}