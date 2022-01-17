package br.com.biblioteca.di

import br.com.biblioteca.data.database.AppDatabase
import br.com.biblioteca.data.database.repository.ParamRepositoryImpl
import br.com.biblioteca.data.database.repository.SuggestionRepositoryImpl
import br.com.biblioteca.data.network.RetrofitConfig
import br.com.biblioteca.data.network.mapper.AuthMapper
import br.com.biblioteca.data.network.mapper.BookMapper
import br.com.biblioteca.data.network.repository.AuthRepositoryImpl
import br.com.biblioteca.data.network.repository.BookRepositoryImpl
import br.com.biblioteca.domain.repository.AuthRepository
import br.com.biblioteca.domain.repository.BookRepository
import br.com.biblioteca.domain.repository.ParamRepository
import br.com.biblioteca.domain.repository.SuggestionRepository
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val dataModule = module {

    single { RetrofitConfig.service }
    single { BookMapper() }
    single { AuthMapper() }
    single { AppDatabase.create(androidApplication())}

    factory<BookRepository>{ BookRepositoryImpl(get(), get())  }
    factory<AuthRepository>{ AuthRepositoryImpl(get(), get()) }
    factory<ParamRepository>{ ParamRepositoryImpl(get()) }
    factory<SuggestionRepository> { SuggestionRepositoryImpl(get()) }

}