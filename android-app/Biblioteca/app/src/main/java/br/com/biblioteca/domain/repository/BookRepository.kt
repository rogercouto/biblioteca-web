package br.com.biblioteca.domain.repository

import br.com.biblioteca.domain.entity.Book

interface BookRepository {
    suspend fun findBooks(text: String): List<Book>
    suspend fun findBooks(text: String, page: Int): List<Book>
    suspend fun findBookById(id: Long): Book
    suspend fun reserveExemplar(numRegistro : Long, usuarioId : Long, token : String)
}