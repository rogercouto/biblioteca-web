package br.com.biblioteca.domain.repository

interface SuggestionRepository {

    fun addSuggestion(suggestion : String)

    fun findSuggestions() : List<String>

}