CREATE TABLE reserva (
  id SERIAL,
  num_registro INT NOT NULL,
  usuario_id INT NOT NULL,
  data_hora timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  data_limite date NOT NULL,
  data_hora_retirada timestamp DEFAULT NULL,
  ativa BOOL NOT NULL DEFAULT TRUE,
  PRIMARY KEY (id),
  CONSTRAINT fk_exemplar_reserva FOREIGN KEY (num_registro) REFERENCES exemplar (num_registro) ON UPDATE CASCADE,
  CONSTRAINT fk_usuario_reserva FOREIGN KEY (usuario_id) REFERENCES usuario (id) ON UPDATE CASCADE
);