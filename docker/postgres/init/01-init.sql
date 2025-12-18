CREATE TABLE IF NOT EXISTS usuarios (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

-- senha: password  (BCrypt) 
INSERT INTO usuarios (username, password)
VALUES (
    'user',
    '$2a$10$dbgkmXOMuEzNekn/9XWCCOeo6FgQAawFgrYtLc7B64oAMDs89lpZ6'
)
ON CONFLICT (username) DO NOTHING;
