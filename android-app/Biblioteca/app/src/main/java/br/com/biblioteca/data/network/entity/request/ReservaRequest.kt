package br.com.biblioteca.data.network.entity.request

class ReservaRequest (
    val exemplar : ExemplarRequest,
    val usuario : UsusarioRequest,
    val dataHora : String
)