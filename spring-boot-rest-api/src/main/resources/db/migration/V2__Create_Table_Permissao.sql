CREATE TABLE IF NOT EXISTS `permissao` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `descricao` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

INSERT INTO `permissao` (`descricao`) VALUES
	('ADMIN'),
	('GERENTE'),
	('ALUNO');