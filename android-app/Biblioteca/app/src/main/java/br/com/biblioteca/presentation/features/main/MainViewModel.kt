package br.com.biblioteca.presentation.features.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.biblioteca.data.database.entity.ParamEntity
import br.com.biblioteca.domain.entity.Auth
import br.com.biblioteca.domain.repository.AuthRepository
import br.com.biblioteca.domain.repository.ParamRepository
import br.com.biblioteca.presentation.base.BaseViewModel
import br.com.biblioteca.presentation.features.auth.*

class MainViewModel(
    private val authRepository : AuthRepository,
    private val paramRepository: ParamRepository
) : BaseViewModel() {

    private val _auth = MutableLiveData<Auth?>()
    val auth : LiveData<Auth?> = _auth

    /**
     * Tenta relogar com o usuario no banco local e armazena o token novo
     * caso de errado ele desloga
     */
    fun trySignInWithCurrentUser(){
        val paramEmail = paramRepository.get(PARAM_EMAIL)
        val paramPassord = paramRepository.get(PARAM_PASSWORD)
        if (paramEmail != null && paramPassord != null){
            launch {
                val nAuth = authRepository.login(paramEmail.value, paramPassord.value)
                paramRepository.update(ParamEntity(PARAM_TOKEN, nAuth.token))
                _auth.postValue(nAuth)
            }
        }
    }

    /**
     * Remove todos os tokens
     */
    fun logout(){
        val paramUserid = paramRepository.get(PARAM_USERID)
        if (paramUserid != null){
            paramRepository.delete(paramUserid)
        }
        val paramUsername = paramRepository.get(PARAM_USERNAME)
        if (paramUsername != null){
            paramRepository.delete(paramUsername)
        }
        val paramEmail = paramRepository.get(PARAM_EMAIL)
        if (paramEmail != null){
            paramRepository.delete(paramEmail)
        }
        val paramPassord = paramRepository.get(PARAM_PASSWORD)
        if (paramPassord != null){
            paramRepository.delete(paramPassord)
        }
        val paramToken = paramRepository.get(PARAM_TOKEN)
        if (paramToken != null){
            paramRepository.delete(paramToken)
        }

    }

}