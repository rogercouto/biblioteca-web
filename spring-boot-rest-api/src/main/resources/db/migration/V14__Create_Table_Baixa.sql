CREATE TABLE baixa (
  id SERIAL,
  num_registro INT NOT NULL,
  data_hora timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  causa VARCHAR(255) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_exemplar_baixa FOREIGN KEY (num_registro) REFERENCES exemplar (num_registro) ON UPDATE CASCADE
);
