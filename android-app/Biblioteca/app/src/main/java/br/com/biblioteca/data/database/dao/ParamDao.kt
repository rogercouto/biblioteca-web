package br.com.biblioteca.data.database.dao

import androidx.room.*
import br.com.biblioteca.data.database.entity.ParamEntity

@Dao
interface ParamDao {

    @Insert
    fun add(paramEntity: ParamEntity)

    @Update
    fun update(paramEntity: ParamEntity)

    @Delete
    fun delete(paramEntity: ParamEntity)

    @Query("SELECT * FROM ParamEntity WHERE ParamEntity.`key` = :key")
    fun getParam(key : String) : ParamEntity?

}