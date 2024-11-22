package com.aloha.security_method.domain;

import java.util.Date;

import lombok.Data;

@Data
public class UserAuth {
    private Long no;
    private String id;
    private Long userNo;
    private String username;
    private String auth;
    private Date createdAt;
    private Date updatedAt;
}