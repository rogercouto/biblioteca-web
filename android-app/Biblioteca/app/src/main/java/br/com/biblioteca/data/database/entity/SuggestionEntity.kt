package br.com.biblioteca.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class SuggestionEntity (

    @PrimaryKey(autoGenerate = false)
    var id : Long?,
    var suggestion : String

)