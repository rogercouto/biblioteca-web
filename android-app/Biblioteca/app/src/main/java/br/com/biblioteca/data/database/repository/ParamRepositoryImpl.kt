package br.com.biblioteca.data.database.repository

import br.com.biblioteca.data.database.AppDatabase
import br.com.biblioteca.data.database.entity.ParamEntity
import br.com.biblioteca.domain.repository.ParamRepository

class ParamRepositoryImpl(appDatabase: AppDatabase) : ParamRepository{

    private val dao = appDatabase.getParamDao()

    override fun add(paramEntity: ParamEntity) {
        dao.add(paramEntity)
    }

    override fun update(paramEntity: ParamEntity) {
        dao.update(paramEntity)
    }

    override fun delete(paramEntity: ParamEntity) {
        dao.delete(paramEntity)
    }

    override fun get(key: String): ParamEntity? {
        return dao.getParam(key)
    }


}