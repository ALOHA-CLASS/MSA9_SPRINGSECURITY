package com.aloha.security_method.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.aloha.security_method.domain.Files;

@Mapper
public interface FileMapper {
    
    public List<Files> list() throws Exception;

    public Files select(String id)  throws Exception;

    public int insert(Files file) throws Exception;

    public int update(Files file) throws Exception;

    public int delete(String id) throws Exception;

    // 부모 테이블 기준 파일 목록
    public List<Files> listByParent(Files file) throws Exception;

    // 부모 테이블 기준 파일 삭제
    public int deleteByParent(Files file) throws Exception;

}
