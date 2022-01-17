CREATE TABLE livro (
  id SERIAL,
  titulo VARCHAR(255) NOT NULL,
  resumo TEXT DEFAULT NULL,
  isbn BIGINT DEFAULT NULL,
  cutter VARCHAR(255) DEFAULT NULL,
  editora_id INT DEFAULT NULL,
  edicao VARCHAR(255) DEFAULT NULL,
  volume VARCHAR(255) DEFAULT NULL,
  num_paginas INT DEFAULT NULL,
  assunto_id INT DEFAULT NULL,
  ano_publicacao SMALLINT DEFAULT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_assunto_livro FOREIGN KEY (assunto_id) REFERENCES assunto (id) ON UPDATE CASCADE,
  CONSTRAINT fk_editora_livro FOREIGN KEY (editora_id) REFERENCES editora (id) ON UPDATE CASCADE
) ;