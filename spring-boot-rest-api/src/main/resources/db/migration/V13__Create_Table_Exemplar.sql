CREATE TABLE IF NOT EXISTS exemplar(
  num_registro INT NOT NULL,
  livro_id INT NOT NULL,
  secao_id INT NOT NULL,
  data_aquisicao DATE NOT NULL,
  origem_id INT NOT NULL,
  fixo BOOL NOT NULL DEFAULT false,
  disponivel BOOL NOT NULL DEFAULT true,
  reservado BOOL NOT NULL DEFAULT false,
  emprestado BOOL NOT NULL DEFAULT false,
  PRIMARY KEY (num_registro),
  CONSTRAINT fk_livro_exemplar FOREIGN KEY (livro_id) REFERENCES livro (id) ON UPDATE CASCADE,
  CONSTRAINT fk_origem_exemplar FOREIGN KEY (origem_id) REFERENCES origem (id) ON UPDATE CASCADE,
  CONSTRAINT fk_secao_exemplar FOREIGN KEY (secao_id) REFERENCES secao (id) ON UPDATE CASCADE
);