package com.aloha.thymeleaf_security.domain;

import lombok.Data;

@Data
public class UserAuth {
    private Long no;
    private String username;
    private String auth;
}