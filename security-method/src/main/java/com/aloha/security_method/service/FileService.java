package com.aloha.security_method.service;

import java.util.List;

import com.aloha.security_method.domain.Files;

public interface FileService {
    
    public List<Files> list() throws Exception;

    public Files select(String id)  throws Exception;

    public int insert(Files file) throws Exception;

    public int update(Files file) throws Exception;

    public int delete(String id) throws Exception;

    // 파일 업로드
    public boolean upload(Files file) throws Exception;

    // 부모 테이블 기준 파일 목록
    public List<Files> listByParent(Files file) throws Exception;

    // 부모 테이블 기준 파일 삭제
    public int deleteByParent(Files file) throws Exception;
}
