-- user_auth

DROP TABLE IF EXISTS `user_auth`;

CREATE TABLE `user_auth` (
      no bigint NOT NULL AUTO_INCREMENT       -- 권한번호
    , `id`	VARCHAR(255)	NOT NULL	COMMENT 'UK'
    , `user_no`	BIGINT	NOT NULL	COMMENT '회원PK'
    , username varchar(100) NOT NULL             -- 아이디
    , auth varchar(100) NOT NULL                -- 권한 (ROLE_USER, ROLE_ADMIN, ...)
    , `created_at`	TIMESTAMP	NOT NULL	DEFAULT CURRENT_TIMESTAMP	COMMENT '등록일자'
	  , `updated_at`	TIMESTAMP	NOT NULL	DEFAULT CURRENT_TIMESTAMP	COMMENT '수정일자'
    , PRIMARY KEY(no)                      
);