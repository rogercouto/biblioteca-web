CREATE TABLE IF NOT EXISTS usuario(
    id SERIAL,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    senha VARCHAR(255) NOT NULL,
    num_tel CHAR(11),
    ativo BOOL NOT NULL DEFAULT true,
    PRIMARY KEY(id),
    UNIQUE(email)
);

INSERT INTO usuario (nome, email, senha, ativo) VALUES
	('Administrador', 'admin@gmail.com', '$2a$16$9qr2tv0HmXbHBsx.TZFjfux742wCZM32a8Wi6iBqwIqaizlHPuxHS', true),
	('Usuário Teste', 'teste@gmail.com', '$2a$16$h4yDQCYTy62R6xrtFDWONeMH3Lim4WQuU/aj8hxW.dJJoeyvtEkhK', true);