CREATE TABLE autor (
  id SERIAL,
  nome VARCHAR(255) NOT NULL,
  sobrenome VARCHAR(255) NOT NULL,
  info TEXT,
  PRIMARY KEY (id)
);