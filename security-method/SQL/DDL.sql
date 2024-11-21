

-- ###################################################################################
-- 외래키 삭제
-- ⭐ 최초 생성시 주석처리(SKIP)
-- SET FOREIGN_KEY_CHECKS = 0;
-- ALTER TABLE `board` DROP FOREIGN KEY `FK_user_TO_board_1`;
-- ALTER TABLE `comments` DROP FOREIGN KEY `FK_board_TO_comments_1`;
-- ALTER TABLE `comments` DROP FOREIGN KEY `FK_user_TO_comments_1`;
-- ###################################################################################




DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `NO` bigint NOT NULL AUTO_INCREMENT,
  `USERNAME` varchar(100) NOT NULL,
  `PASSWORD` varchar(200) NOT NULL,
  `NAME` varchar(100) NOT NULL,
  `EMAIL` varchar(200) DEFAULT NULL,
  `CREATED_AT` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `UPDATED_AT` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `ENABLED` int DEFAULT 1,
  PRIMARY KEY (`NO`)
) COMMENT='회원';



DROP TABLE IF EXISTS `user_auth`;

CREATE TABLE `user_auth` (
      no bigint NOT NULL AUTO_INCREMENT       -- 권한번호
    , username varchar(100) NOT NULL             -- 아이디
    , auth varchar(100) NOT NULL                -- 권한 (ROLE_USER, ROLE_ADMIN, ...)
    , PRIMARY KEY(no)                      
);


DROP TABLE IF EXISTS persistent_logins;

create table persistent_logins (
	  username varchar(64) not null
	, series varchar(64) primary key
	, token varchar(64) not null
	, last_used timestamp not null)
;


DROP TABLE IF EXISTS `board`;

CREATE TABLE `board` (
	`no`	BIGINT	NOT NULL AUTO_INCREMENT PRIMARY KEY	COMMENT 'PK',
	`id`	VARCHAR(255)	NOT NULL	COMMENT 'UK',
	`title`	VARCHAR(100)	NOT NULL	COMMENT '제목',
    `user_no`	BIGINT	NOT NULL	COMMENT '회원PK',
	`content`	TEXT	NULL	COMMENT '내용',
	`created_at`	TIMESTAMP	NOT NULL	DEFAULT CURRENT_TIMESTAMP	COMMENT '등록일자',
	`updated_at`	TIMESTAMP	NOT NULL	DEFAULT CURRENT_TIMESTAMP	COMMENT '수정일자'
);


ALTER TABLE `board` ADD CONSTRAINT `FK_user_TO_board_1` FOREIGN KEY (
	`user_no`
)
REFERENCES `user` (
	`no`
)
ON DELETE CASCADE
;


DROP TABLE IF EXISTS `files`;

CREATE TABLE `files` (
	`no`	BIGINT	NOT NULL AUTO_INCREMENT PRIMARY KEY	COMMENT 'PK',
	`id`	VARCHAR(255)	NOT NULL	COMMENT 'UK',
	`file_name`	TEXT	NOT NULL	COMMENT '파일명',
	`file_path`	TEXT	NOT NULL	COMMENT '파일경로',
	`file_size`	BIGINT	NULL	COMMENT '용량',
	`type`	ENUM('main','sub')	NOT NULL	DEFAULT 'main'	COMMENT '타입',
	`parent_table`	VARCHAR(100)	NOT NULL	COMMENT '부모테이블',
	`parent_no`	BIGINT	NOT NULL	COMMENT '부모PK',
	`created_at`	TIMESTAMP	NOT NULL	DEFAULT CURRENT_TIMESTAMP	COMMENT '등록일자',
	`updated_at`	TIMESTAMP	NOT NULL	DEFAULT CURRENT_TIMESTAMP	COMMENT '수정일자'
);

DROP TABLE IF EXISTS `comments`;

CREATE TABLE `comments` (
	`no`	BIGINT	NOT NULL AUTO_INCREMENT PRIMARY KEY	COMMENT 'PK',
	`id`	VARCHAR(255)	NOT NULL	COMMENT 'UK',
	`board_no`	BIGINT	NOT NULL	COMMENT '게시글 PK',
	`parent_no`	BIGINT	NOT NULL	COMMENT '부모 댓글 번호',
    `user_no`	BIGINT	NOT NULL	COMMENT '회원PK',
	`content`	TEXT	NULL	COMMENT '내용',
	`created_at`	TIMESTAMP	NOT NULL	DEFAULT CURRENT_TIMESTAMP	COMMENT '등록일자',
	`updated_at`	TIMESTAMP	NOT NULL	DEFAULT CURRENT_TIMESTAMP	COMMENT '수정일자'
);



ALTER TABLE `comments` ADD CONSTRAINT `FK_board_TO_comments_1` FOREIGN KEY (
	`board_no`
)
REFERENCES `board` (
	`no`
)
ON DELETE CASCADE
;

ALTER TABLE `comments` ADD CONSTRAINT `FK_user_TO_comments_1` FOREIGN KEY (
	`user_no`
)
REFERENCES `user` (
	`no`
)
ON DELETE CASCADE
;












