
SELECT *
FROM user u LEFT JOIN user_auth auth ON u.username = auth.username
WHERE u.username = 'admin'
;