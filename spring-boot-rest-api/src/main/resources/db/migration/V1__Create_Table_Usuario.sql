CREATE TABLE IF NOT EXISTS `usuario`(
    `id` INT NOT NULL AUTO_INCREMENT,
    `nome` VARCHAR(255) NOT NULL,
    `email` VARCHAR(255) NOT NULL,
    `senha` VARCHAR(255) NOT NULL,
    `ativo` bit(1) DEFAULT NULL,
    PRIMARY KEY(`id`),
    UNIQUE KEY `uk_email`(`email`)
)ENGINE=InnoDB;

INSERT INTO `usuario` (`nome`, `email`, `senha`, `ativo`) VALUES
	('Administrador', 'admin@gmail.com', '$2a$16$9qr2tv0HmXbHBsx.TZFjfux742wCZM32a8Wi6iBqwIqaizlHPuxHS', b'1'),
	('Roger Couto', 'rogerecouto@gmail.com', '$2a$16$h4yDQCYTy62R6xrtFDWONeMH3Lim4WQuU/aj8hxW.dJJoeyvtEkhK', b'1');