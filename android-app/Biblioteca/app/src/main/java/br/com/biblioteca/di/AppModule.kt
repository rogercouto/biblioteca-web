package br.com.biblioteca.di

import org.koin.core.module.Module

val appModules : List<Module> = listOf(dataModule, presentationModule)