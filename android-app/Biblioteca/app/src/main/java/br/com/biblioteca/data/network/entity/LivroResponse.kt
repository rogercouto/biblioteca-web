package br.com.biblioteca.data.network.entity

class LivroResponse (
    val id: Long,
    val titulo: String,
    val resumo: String?,
    val editora: EditoraResponse?,
    val assunto: AssuntoResponse,
    val nomesAutores: String,
    val exemplares: List<ExemplarResponse>
)