package com.aloha.security5.domain;

import lombok.Data;

@Data
public class UserAuth {
    private Long no;
    private String username;
    private String auth;
}
