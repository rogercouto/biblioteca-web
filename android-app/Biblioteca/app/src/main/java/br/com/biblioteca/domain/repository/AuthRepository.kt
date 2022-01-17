package br.com.biblioteca.domain.repository

import br.com.biblioteca.domain.entity.Auth

interface AuthRepository {
    suspend fun login(user: String, password: String): Auth
}