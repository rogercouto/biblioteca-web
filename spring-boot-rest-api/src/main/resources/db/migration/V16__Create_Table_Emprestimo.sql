CREATE TABLE emprestimo (
  id SERIAL,
  data_hora timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  usuario_id INT NOT NULL,
  num_registro INT NOT NULL,
  renovacoes INT DEFAULT NULL,
  data_hora_devolucao timestamp DEFAULT NULL,
  prazo DATE NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_exemplar_emprestimo FOREIGN KEY (num_registro) REFERENCES exemplar (num_registro) ON UPDATE CASCADE,
  CONSTRAINT fk_usuario_emprestimo FOREIGN KEY (usuario_id) REFERENCES usuario (id) ON UPDATE CASCADE
);
