package br.com.biblioteca.data.network.repository

import br.com.biblioteca.data.network.BibliotecaApi
import br.com.biblioteca.data.network.entity.request.ExemplarRequest
import br.com.biblioteca.data.network.entity.request.ReservaRequest
import br.com.biblioteca.data.network.entity.request.UsusarioRequest
import br.com.biblioteca.data.network.mapper.BookMapper
import br.com.biblioteca.domain.entity.Book
import br.com.biblioteca.domain.repository.BookRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.RuntimeException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class BookRepositoryImpl(
    private val api : BibliotecaApi,
    private val bookMapper: BookMapper
) : BookRepository{

    override suspend fun findBooks(text: String): List<Book> {
        return withContext(Dispatchers.IO){
            bookMapper.toDomain(api.findLivros(text))
        }
    }

    override suspend fun findBooks(text: String, page: Int): List<Book> {
        return withContext(Dispatchers.IO){
            bookMapper.toDomain(api.findLivros(text, page))
        }
    }

    override suspend fun findBookById(id: Long): Book {
        return withContext(Dispatchers.IO){
            bookMapper.toDomain(api.findLivroById(id))
        }
    }

    override suspend fun reserveExemplar(numRegistro : Long, usuarioId : Long, token : String) {
        val agora = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        val dt = formatter.format(agora)
        val reservaRequest = ReservaRequest(
            exemplar = ExemplarRequest(numRegistro),
            usuario = UsusarioRequest(usuarioId),
            dataHora = dt
        )
        withContext(Dispatchers.IO){
            api.reseve(token, reservaRequest)
        }
    }


}