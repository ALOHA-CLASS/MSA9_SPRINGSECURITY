package com.aloha.security_method.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 검색 옵션
 * - keyword        : 검색어
 * - code           : 옵션 코드
 *  * 0 : 전체
 *  * 1 : 제목
 *  * 2 : 내용
 *  * 3 : 제목+내용
 *  * 4 : 작성자
 * 
 *  - orderCode         : 순서 옵션 코드
 *  * 0 : 최신순 (등록일자)
 *  * 1 : 제목순
 */
@Data
@AllArgsConstructor
public class Option {
    String keyword;             // 검색어
    int code;                   // 검색 옵션 코드
    int orderCode;              // 순서 옵션 코드

    public Option() {
        this.keyword = "";
    }
}   
