CREATE TABLE IF NOT EXISTS `exemplar`(
  `num_registro` INT NOT NULL,
  `livro_id` INT NOT NULL,
  `secao_id` INT NOT NULL,
  `data_aquisicao` DATE NOT NULL,
  `origem_id` INT NOT NULL,
  `fixo` BIT(1) NOT NULL DEFAULT b'0',
  `disponivel` BIT(1) NOT NULL DEFAULT b'1',
  `reservado` BIT(1) NOT NULL DEFAULT b'0',
  `emprestado` BIT(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`num_registro`),
  KEY `fk_livro_exemplar` (`livro_id`),
  KEY `fk_secao_exemplar` (`secao_id`),
  KEY `fk_origem_exemplar` (`origem_id`),
  CONSTRAINT `fk_livro_exemplar` FOREIGN KEY (`livro_id`) REFERENCES `livro` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `fk_origem_exemplar` FOREIGN KEY (`origem_id`) REFERENCES `origem` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `fk_secao_exemplar` FOREIGN KEY (`secao_id`) REFERENCES `secao` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;