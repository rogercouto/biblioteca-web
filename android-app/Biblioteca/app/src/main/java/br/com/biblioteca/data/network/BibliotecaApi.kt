package br.com.biblioteca.data.network

import br.com.biblioteca.data.network.entity.LivroResponse
import br.com.biblioteca.data.network.entity.request.LoginRequest
import br.com.biblioteca.data.network.entity.LoginResponse
import br.com.biblioteca.data.network.entity.request.ReservaRequest
import retrofit2.http.*

interface BibliotecaApi {


    @GET("/livros")
    suspend fun findLivros(@Query("find")text: String) : List<LivroResponse>

    @GET("/livros")
    suspend fun findLivros(@Query("find")text: String, @Query("page")page: Int) : List<LivroResponse>

    @GET("/livros/{id}")
    suspend fun findLivroById(@Path("id")id : Long) : LivroResponse

    @POST("/auth/signin")
    suspend fun login(@Body loginRequest: LoginRequest) : LoginResponse

    @POST("/reservas")
    suspend fun reseve(@Header(value = "Authorization") authorization : String, @Body reservaRequest: ReservaRequest)

}