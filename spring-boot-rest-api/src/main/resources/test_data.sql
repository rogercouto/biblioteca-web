drop database biblioteca;
create database biblioteca;
use biblioteca;

select * from usuario;
select * from editora;
select * from assunto;
select * from livro;
select * from categoria;
select * from autor;
select * from categoria_livro;
select * from autor_livro;
select * from secao;
select * from origem;
select * from exemplar;

insert into editora(nome) values('Editora teste');

insert into assunto(descricao) values ('Assunto teste');

insert into livro(titulo, resumo, isbn, cutter, editora_id, edicao, volume, num_paginas, assunto_id, ano_publicacao)
values('Livro teste', 'Resumo do livro teste', 123, 'cutter', 1, '1ª', 'único', 100, 1, 2020);

insert into categoria(descricao) values ('Categoria teste'),('Segunda categoria');

insert into autor(nome, sobrenome) values ('Fulano', 'de Tal'),('Ciclano', 'Teste');

insert into categoria_livro values(1,1),(2,1);

insert into autor_livro values(1,1),(2,1);

insert into secao(descricao) values('Seção A'),('Seção B');

insert into origem(descricao) values('Aquisição'),('Doação');

insert into exemplar values(1001, 1, 1, '2009-3-30', 1, b'0', b'1', b'0', b'0');



