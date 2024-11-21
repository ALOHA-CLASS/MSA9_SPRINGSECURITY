package com.aloha.security_method.domain;

import java.util.Date;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Comments {

    private int no;
    private String id;
    private int boardNo;
    private int parentNo;           // 부모 댓글 번호
    private String writer;
    private String content;
    private Date createdAt;
    private Date updatedAt;

    public Comments() {
        this.id = UUID.randomUUID().toString();
    }
    
}
