drop database biblioteca;
create database biblioteca;
create database biblioteca_test;
use biblioteca;

select * from usuario;

delete from usuario where id = 3;

use biblioteca;

select * from editora;
insert into editora(nome) values('Editora teste');
select * from assunto;
insert into assunto(descricao) values ('Assunto teste');
select * from livro;

alter table livro drop ano;
insert into livro(titulo, resumo, isbn, cutter, editora_id, edicao, volume, num_paginas, assunto_id, ano_publicacao)
values('Livro teste', 'Resumo do livro teste', 123, 'cutter', 1, '1ª', 'único', 100, 1, 2020);

select * from categoria;
insert into categoria(descricao) values ('Categoria teste'),('Segunda categoria');

select * from autor;
insert into autor(nome, sobrenome) values ('Fulano', 'de Tal'),('Ciclano', 'Teste');

select * from categoria_livro;
insert into categoria_livro values(1,1),(2,1);

select * from autor_livro;
insert into autor_livro values(1,1),(2,1);

select * from secao;
insert into secao(nome) values('Seção A'),('Seção B');

select * from origem;
insert into origem(descricao) values('Aquisição'),('Doação');

select * from exemplar;



