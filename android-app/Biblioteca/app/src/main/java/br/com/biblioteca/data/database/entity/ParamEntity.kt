package br.com.biblioteca.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class ParamEntity (
    @PrimaryKey val key : String,
    var value: String
)