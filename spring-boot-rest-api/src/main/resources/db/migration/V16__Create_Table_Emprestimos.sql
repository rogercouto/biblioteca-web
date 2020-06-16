CREATE TABLE `emprestimo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `data_hora` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `usuario_id` int(11) NOT NULL,
  `num_registro` int(11) NOT NULL,
  `renovacoes` int(11) DEFAULT NULL,
  `data_hora_devolucao` datetime DEFAULT NULL,
  `multa` decimal(20,2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_usuario_emprestimo` (`usuario_id`),
  KEY `fk_exemplar_emprestimo` (`num_registro`),
  CONSTRAINT `fk_exemplar_emprestimo` FOREIGN KEY (`num_registro`) REFERENCES `exemplar` (`num_registro`) ON UPDATE CASCADE,
  CONSTRAINT `fk_usuario_emprestimo` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB;
