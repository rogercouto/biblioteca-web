CREATE TABLE IF NOT EXISTS permissao (
  id SERIAL,
  descricao VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (id)
) ;


INSERT INTO permissao (descricao) VALUES
	('ADMIN'),
	('GERENTE'),
	('ALUNO');