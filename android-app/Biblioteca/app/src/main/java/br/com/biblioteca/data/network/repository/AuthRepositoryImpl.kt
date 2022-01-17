package br.com.biblioteca.data.network.repository

import br.com.biblioteca.data.network.BibliotecaApi
import br.com.biblioteca.data.network.entity.request.LoginRequest
import br.com.biblioteca.data.network.mapper.AuthMapper
import br.com.biblioteca.domain.entity.Auth
import br.com.biblioteca.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepositoryImpl(
    private val api : BibliotecaApi,
    private val authMapper: AuthMapper
):AuthRepository{

    override suspend fun login(email: String, password: String): Auth {
        return withContext(Dispatchers.IO){
            authMapper.toDomain(api.login(LoginRequest(email, password)))
        }
    }

}