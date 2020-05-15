CREATE TABLE `baixa` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `num_registro` int(11) NOT NULL,
  `data_hora` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `causa` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_exemplar_baixa` (`num_registro`),
  CONSTRAINT `fk_exemplar_baixa` FOREIGN KEY (`num_registro`) REFERENCES `exemplar` (`num_registro`) ON UPDATE CASCADE
) ENGINE=InnoDB;
