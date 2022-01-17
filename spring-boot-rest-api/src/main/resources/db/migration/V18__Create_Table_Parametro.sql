CREATE TABLE parametro(
    chave varchar(255) NOT NULL,
    valor varchar(255) NOT NULL,
    editavel bool NOT NULL DEFAULT TRUE,
    PRIMARY KEY(chave)
);

INSERT INTO parametro values
('diasEmprestimo', '7', true),
('diasReserva', '2', true),
('limiteRenov', '2', true),
('multaPorDiaAtraso','2.00', true),
('limiteReservas','3', true),
('limiteEmprestimos','3', true);