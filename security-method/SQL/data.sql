-- 회원
-- ###################################################################################
TRUNCATE TABLE `user`;
TRUNCATE TABLE `user_auth`;

-- BCryptPasswordEncoder - 암호화 시
-- 사용자
INSERT INTO user ( username, password, name, email )
VALUES ( 'user', '$2a$12$TrN..KcVjciCiz.5Vj96YOBljeVTTGJ9AUKmtfbGpgc9hmC7BxQ92', '사용자', 'user@mail.com' );
-- 관리자
INSERT INTO user ( username, password, name, email )
VALUES ( 'admin', '$2a$12$TrN..KcVjciCiz.5Vj96YOBljeVTTGJ9AUKmtfbGpgc9hmC7BxQ92', '관리자', 'admin@mail.com' );
-- 테스트
INSERT INTO user ( username, password, name, email )
VALUES ( 'test', '$2a$12$TrN..KcVjciCiz.5Vj96YOBljeVTTGJ9AUKmtfbGpgc9hmC7BxQ92', '테스트', 'test@mail.com' );

-- 권한
-- 사용자 
-- * 권한 : ROLE_USER
INSERT INTO user_auth ( username,  auth )
VALUES ( 'user', 'ROLE_USER' );
-- 관리자
-- * 권한 : ROLE_USER, ROLE_ADMIN
INSERT INTO user_auth ( username,  auth )
VALUES ( 'admin', 'ROLE_USER' );

INSERT INTO user_auth ( username,  auth )
VALUES ( 'admin', 'ROLE_ADMIN' );
-- 사용자 
-- * 권한 : ROLE_USER
INSERT INTO user_auth ( username,  auth )
VALUES ( 'test', 'ROLE_USER' );
-- ###################################################################################


-- 게시판
-- ###################################################################################
TRUNCATE TABLE `board`;
INSERT INTO board ( id, title, user_no, content )
VALUES ( UUID(), '제목1', FLOOR(1 + (RAND() * 3)), '내용1' )
    , ( UUID(), '제목2', FLOOR(1 + (RAND() * 3)), '내용2' )
    , ( UUID(), '제목3', FLOOR(1 + (RAND() * 3)), '내용3' )
    , ( UUID(), '제목4', FLOOR(1 + (RAND() * 3)), '내용4' )
    , ( UUID(), '제목5', FLOOR(1 + (RAND() * 3)), '내용5' )
    , ( UUID(), '제목6', FLOOR(1 + (RAND() * 3)), '내용6' )
    , ( UUID(), '제목7', FLOOR(1 + (RAND() * 3)), '내용7' )
    , ( UUID(), '제목8', FLOOR(1 + (RAND() * 3)), '내용8' )
    , ( UUID(), '제목9', FLOOR(1 + (RAND() * 3)), '내용9' )
    , ( UUID(), '제목10', FLOOR(1 + (RAND() * 3)), '내용10' )
;
-- ###################################################################################



-- 댓글
-- ###################################################################################
TRUNCATE TABLE `comments`;

INSERT INTO comments ( id, board_no, parent_no, user_no, content )
VALUES (UUID(),  FLOOR(1 + (RAND() * 10)), 0,  FLOOR(1 + (RAND() * 3)), '댓글 내용1')
    , (UUID(),  FLOOR(1 + (RAND() * 10)), 0, FLOOR(1 + (RAND() * 3)), '댓글 내용2')
    , (UUID(),  FLOOR(1 + (RAND() * 10)), 0, FLOOR(1 + (RAND() * 3)), '댓글 내용3')
    , (UUID(),  FLOOR(1 + (RAND() * 10)), 0, FLOOR(1 + (RAND() * 3)), '댓글 내용4')
    , (UUID(),  FLOOR(1 + (RAND() * 10)), 0, FLOOR(1 + (RAND() * 3)), '댓글 내용5')
    , (UUID(),  FLOOR(1 + (RAND() * 10)), 0, FLOOR(1 + (RAND() * 3)), '댓글 내용6')
    , (UUID(),  FLOOR(1 + (RAND() * 10)), 0, FLOOR(1 + (RAND() * 3)), '댓글 내용7')
    , (UUID(),  FLOOR(1 + (RAND() * 10)), 0, FLOOR(1 + (RAND() * 3)), '댓글 내용8')
    , (UUID(),  FLOOR(1 + (RAND() * 10)), 0, FLOOR(1 + (RAND() * 3)), '댓글 내용9')
    , (UUID(),  FLOOR(1 + (RAND() * 10)), 0, FLOOR(1 + (RAND() * 3)), '댓글 내용10')
;

-- ###################################################################################