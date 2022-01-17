package br.com.biblioteca.domain.entity

class Book (
    var id: Long,
    var title: String,
    var abstract: String?,
    var publisher: String?,
    var subject: String,
    var authors: String,
    var exemplars: List<Exemplar>
){
    fun getAvaliableExemplars() : Int{
        return exemplars.filter { it.disponivel }.count()
    }

    fun getLastNumRegistro(): Long {
        var numRegistro : Long = 0
        exemplars.forEach{
            if (it.disponivel)
                numRegistro = it.numRegistro
        }
        return numRegistro
    }

    fun setLastReserved(){
        exemplars.filter { it.disponivel }.last().disponivel = false
    }
}