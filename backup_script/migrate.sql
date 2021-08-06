insert into origem values(3, 'Doação apostila');

select * from biblioteca_b.editora;
select * from biblioteca_a.editora;
truncate table biblioteca_a.editora;
insert into biblioteca_a.editora(id, nome) select editora_id, nome from biblioteca_b.editora;

select * from biblioteca_b.secao;
select * from biblioteca_a.secao;

truncate table biblioteca_a.secao;
insert into biblioteca_a.secao(id, nome) select secao_id, descricao from biblioteca_b.secao;

SET FOREIGN_KEY_CHECKS = 0; 
truncate table biblioteca_a.assunto;

select * from biblioteca_b.assunto;
select * from biblioteca_a.assunto;
insert into biblioteca_a.assunto(id, descricao, cores, cdu) select assunto_id, descricao, cores, cdu from biblioteca_b.assunto;

select * from biblioteca_b.livro;
select * from biblioteca_a.livro;

insert into biblioteca_a.livro(id, titulo, resumo, isbn, cutter, editora_id, edicao, volume, num_paginas, assunto_id, ano_publicacao) 
select livro_id, titulo, resumo, isbn, cutter, editora_id, edicao, volume, num_paginas, assunto_id, ano_publicacao from biblioteca_b.livro;

select * from biblioteca_b.exemplar;
select * from biblioteca_a.exemplar;

update biblioteca_b.exemplar set origem_id = 1 where origem_id is null;

select num_registro, livro_id, secao_id, data_aquisicao, origem_id, fixo from biblioteca_b.exemplar;

insert into biblioteca_a.exemplar(num_registro, livro_id, secao_id, data_aquisicao, origem_id, fixo)
select num_registro, livro_id, secao_id, data_aquisicao, origem_id, fixo from biblioteca_b.exemplar;

select * from biblioteca_b.categoria;
select * from biblioteca_a.categoria;
insert into biblioteca_a.categoria(id, descricao) select categoria_id, descricao from biblioteca_b.categoria;

select * from biblioteca_b.categoria_livro;
select * from biblioteca_a.categoria_livro;
insert into biblioteca_a.categoria_livro select * from biblioteca_b.categoria_livro;

select * from biblioteca_b.autor;
select * from biblioteca_a.autor;
insert into biblioteca_a.autor(id, nome, sobrenome, info) select autor_id, nome, sobrenome, info from biblioteca_b.autor;

select * from biblioteca_b.autor_livro;
select * from biblioteca_a.autor_livro;
insert into biblioteca_a.autor_livro select * from biblioteca_b.autor_livro;

use biblioteca_a;

select * from secao;
select * from origem;
select * from livro;
select * from exemplar;
select * from editora;
select * from categoria;
select * from categoria_livro;
select * from autor;
select * from autor_livro;
select * from assunto;





