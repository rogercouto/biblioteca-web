CREATE TABLE IF NOT EXISTS `permissao_usuario` (
  `permissao_id` INT NOT NULL,
  `usuario_id` INT NOT NULL,
  PRIMARY KEY (`usuario_id`,`permissao_id`),
  KEY `fk_permissao_usuario_permissao` (`permissao_id`),
  CONSTRAINT `fk_permissao_usuario` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`),
  CONSTRAINT `fk_permissao_usuario_permissao` FOREIGN KEY (`permissao_id`) REFERENCES `permissao` (`id`)
) ENGINE=InnoDB;

INSERT INTO `permissao_usuario` (`permissao_id`, `usuario_id`) VALUES
	(1, 1),
	(2, 1),
	(3, 1),
	(2, 2),
	(3, 2);