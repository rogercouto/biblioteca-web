CREATE TABLE autor_livro (
  autor_id INT NOT NULL,
  livro_id INT NOT NULL,
  PRIMARY KEY (autor_id,livro_id),
  CONSTRAINT fk_autor_livro FOREIGN KEY (autor_id) REFERENCES autor (id) ON UPDATE CASCADE,
  CONSTRAINT fk_livro_autor FOREIGN KEY (livro_id) REFERENCES livro (id) ON UPDATE CASCADE
) ;