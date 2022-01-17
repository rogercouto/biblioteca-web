package br.com.biblioteca.data.database.repository

import br.com.biblioteca.data.database.AppDatabase
import br.com.biblioteca.data.database.entity.SuggestionEntity
import br.com.biblioteca.domain.repository.SuggestionRepository

class SuggestionRepositoryImpl(appDatabase: AppDatabase) : SuggestionRepository {

    private val dao = appDatabase.getSuggestionDao()

    override fun addSuggestion(suggestion: String) {
        dao.add(SuggestionEntity(null, suggestion))
    }

    override fun findSuggestions(): List<String> {
        return dao.findSuggestions().map { it.suggestion }
    }
}