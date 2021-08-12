CREATE TABLE `reserva` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `num_registro` int(11) NOT NULL,
  `usuario_id` int(11) NOT NULL,
  `data_hora` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `data_limite` date NOT NULL,
  `data_hora_retirada` datetime DEFAULT NULL,
  `ativa` bit(1) NOT NULL DEFAULT b'1',
  PRIMARY KEY (`id`),
  KEY `fk_exemplar_reserva` (`num_registro`),
  KEY `fk_usuario_reserva` (`usuario_id`),
  CONSTRAINT `fk_exemplar_reserva` FOREIGN KEY (`num_registro`) REFERENCES `exemplar` (`num_registro`) ON UPDATE CASCADE,
  CONSTRAINT `fk_usuario_reserva` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB;