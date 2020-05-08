CREATE TABLE `livro` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `titulo` VARCHAR(255) NOT NULL,
  `resumo` TEXT,
  `isbn` BIGINT DEFAULT NULL,
  `cutter` VARCHAR(255) DEFAULT NULL,
  `editora_id` INT DEFAULT NULL,
  `edicao` VARCHAR(255) DEFAULT NULL,
  `volume` VARCHAR(255) DEFAULT NULL,
  `num_paginas` INT DEFAULT NULL,
  `assunto_id` INT DEFAULT NULL,
  `ano_publicacao` SMALLINT DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_editora_livro` (`editora_id`),
  KEY `fk_assunto_livro` (`assunto_id`),
  CONSTRAINT `fk_assunto_livro` FOREIGN KEY (`assunto_id`) REFERENCES `assunto` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `fk_editora_livro` FOREIGN KEY (`editora_id`) REFERENCES `editora` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB;