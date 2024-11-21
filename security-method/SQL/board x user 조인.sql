SELECT b.*
    ,u.no user_no
    ,u.name
    ,u.username 
FROM board b 
    JOIN user u ON b.user_no = u.no
;