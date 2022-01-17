package br.com.biblioteca.di

import br.com.biblioteca.presentation.features.main.MainViewModel
import br.com.biblioteca.presentation.features.auth.AuthViewModel
import br.com.biblioteca.presentation.features.books.BookViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel { BookViewModel(get(), get()) }
    viewModel { AuthViewModel(get(), get()) }
    viewModel { MainViewModel(get(), get()) }
}