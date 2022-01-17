package br.com.biblioteca.domain.repository

import br.com.biblioteca.data.database.entity.ParamEntity

interface ParamRepository {

    fun add(paramEntity: ParamEntity)
    fun update(paramEntity: ParamEntity)
    fun delete(paramEntity: ParamEntity)
    fun get(key: String) : ParamEntity?

}