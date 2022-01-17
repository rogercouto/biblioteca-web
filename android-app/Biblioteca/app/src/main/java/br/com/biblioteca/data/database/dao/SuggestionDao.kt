package br.com.biblioteca.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.com.biblioteca.data.database.entity.SuggestionEntity

@Dao
interface SuggestionDao {

    @Insert
    fun add(suggestionEntity: SuggestionEntity)

    @Query("SELECT * FROM SuggestionEntity ORDER BY id DESC LIMIT 10")
    fun findSuggestions() : List<SuggestionEntity>

}