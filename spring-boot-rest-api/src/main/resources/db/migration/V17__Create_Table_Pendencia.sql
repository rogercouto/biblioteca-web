CREATE TABLE `pendencia`(
	`id` int(11) NOT NULL AUTO_INCREMENT,
	`usuario_id` int(11) NOT NULL,
	`valor` decimal(20,2) NOT NULL,
	`descricao` VARCHAR(255),
	`data_hora_lancamento` timestamp NOT NULL,
	`data_hora_pagamento` datetime,
	`emprestimo_id` int(11),
	PRIMARY KEY(`id`),
	KEY `fk_usuario_pendencia` (`usuario_id`),
	KEY `fk_emprestimo_pendencia` (`emprestimo_id`),
	CONSTRAINT `fk_usuario_pendencia` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`) ON UPDATE CASCADE,
	CONSTRAINT `fk_emprestimo_pendencia` FOREIGN KEY (`emprestimo_id`) REFERENCES `emprestimo` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB;

