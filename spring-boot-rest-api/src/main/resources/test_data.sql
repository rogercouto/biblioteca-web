drop database biblioteca;
create database biblioteca;
use biblioteca;

create database biblioteca_test;
use biblioteca_test;

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

insert into secao(nome) values('Seção A'),('Seção B');

insert into exemplar(num_registro, livro_id, secao_id, data_aquisicao, origem_id) values(1001, 1,  1, '2009-3-30', 1);

