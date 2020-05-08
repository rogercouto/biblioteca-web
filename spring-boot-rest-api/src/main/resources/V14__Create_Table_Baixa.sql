CREATE TABLE `baixa` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `exemplar_id` INT NOT NULL,
  `data_hora` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `causa` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_exemplar_baixa` (`exemplar_id`),
  CONSTRAINT `fk_exemplar_baixa` FOREIGN KEY (`exemplar_id`) REFERENCES `exemplar` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB;