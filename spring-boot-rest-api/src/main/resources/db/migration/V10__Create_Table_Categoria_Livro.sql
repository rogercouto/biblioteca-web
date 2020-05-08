CREATE TABLE `categoria_livro` (
  `categoria_id` INT NOT NULL,
  `livro_id` BIGINT NOT NULL,
  PRIMARY KEY (`categoria_id`,`livro_id`),
  KEY `fk_livro_categoria` (`livro_id`),
  CONSTRAINT `fk_categoria_livro` FOREIGN KEY (`categoria_id`) REFERENCES `categoria` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `fk_livro_categoria` FOREIGN KEY (`livro_id`) REFERENCES `livro` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB;