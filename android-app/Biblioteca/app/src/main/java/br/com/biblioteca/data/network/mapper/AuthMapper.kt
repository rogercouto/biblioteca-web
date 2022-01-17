package br.com.biblioteca.data.network.mapper

import br.com.biblioteca.data.network.entity.LoginResponse
import br.com.biblioteca.domain.entity.Auth
import br.com.biblioteca.domain.entity.User

class AuthMapper:DomainMapper<LoginResponse, Auth> {

    override fun toDomain(from: LoginResponse) = Auth (
        user = User(
            id = from.usuario.id,
            name = from.usuario.nome,
            email = from.usuario.email,
            password = null
        ),
        token = from.token
    )

    override fun toDomain(from: List<LoginResponse>) = from.map{
        toDomain(it)
    }
}