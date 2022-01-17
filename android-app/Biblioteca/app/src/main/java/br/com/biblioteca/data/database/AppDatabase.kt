package br.com.biblioteca.data.database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.com.biblioteca.data.database.dao.ParamDao
import br.com.biblioteca.data.database.dao.SuggestionDao
import br.com.biblioteca.data.database.entity.ParamEntity
import br.com.biblioteca.data.database.entity.SuggestionEntity

@Database(entities = [ParamEntity::class, SuggestionEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getParamDao() : ParamDao

    abstract fun getSuggestionDao() : SuggestionDao

    companion object{
        private const val DATABASE_NAME = "library-db"
        fun create(application: Application) : AppDatabase{
            return Room.databaseBuilder(
                application,
                AppDatabase::class.java,
                DATABASE_NAME
            )
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
        }
    }

}