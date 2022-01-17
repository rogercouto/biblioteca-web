package br.com.biblioteca.data.network.mapper

import br.com.biblioteca.data.network.entity.LivroResponse
import br.com.biblioteca.domain.entity.Book
import br.com.biblioteca.domain.entity.Exemplar

class BookMapper: DomainMapper<LivroResponse, Book> {

    override fun toDomain(from: LivroResponse)= Book (
        id = from.id,
        title = from.titulo,
        abstract = from.resumo,
        publisher = from.editora?.nome,
        subject = from.assunto.descricao,
        authors = from.nomesAutores,
        exemplars = from.exemplares.map {
            Exemplar(it.numRegistro, it.situacao.compareTo("Disponível") == 0)
        }
    )

    override fun toDomain(from: List<LivroResponse>) = from.map{
        toDomain(it)
    }
}