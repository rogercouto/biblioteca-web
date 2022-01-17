CREATE TABLE origem (
  id SERIAL,
  descricao VARCHAR(255) NOT NULL,
  PRIMARY KEY (id)
);

INSERT INTO origem(descricao) VALUES('Aquisição'),('Doação'),('Doação Apostila');