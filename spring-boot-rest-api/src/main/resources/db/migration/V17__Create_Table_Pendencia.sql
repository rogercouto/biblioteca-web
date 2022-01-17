CREATE TABLE pendencia(
	id SERIAL,
	usuario_id INT NOT NULL,
	valor decimal(20,2) NOT NULL,
	descricao VARCHAR(255),
	data_hora_lancamento timestamp NOT NULL,
	data_hora_pagamento timestamp,
	emprestimo_id INT,
	PRIMARY KEY(id),
	CONSTRAINT fk_usuario_pendencia FOREIGN KEY (usuario_id) REFERENCES usuario (id) ON UPDATE CASCADE,
	CONSTRAINT fk_emprestimo_pendencia FOREIGN KEY (emprestimo_id) REFERENCES emprestimo (id) ON UPDATE CASCADE
);

