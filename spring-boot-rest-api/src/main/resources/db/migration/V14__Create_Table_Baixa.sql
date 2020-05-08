CREATE TABLE `baixa` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `num_registro` BIGINT NOT NULL,
  `data_hora` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `causa` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_exemplar_baixa` (`num_registro`),
  CONSTRAINT `fk_exemplar_baixa` FOREIGN KEY (`num_registro`) REFERENCES `exemplar` (`num_registro`) ON UPDATE CASCADE
) ENGINE=InnoDB;