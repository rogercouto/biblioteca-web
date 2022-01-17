package br.com.biblioteca.presentation.features.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.biblioteca.data.database.entity.ParamEntity
import br.com.biblioteca.domain.entity.Auth
import br.com.biblioteca.domain.repository.AuthRepository
import br.com.biblioteca.domain.repository.ParamRepository
import br.com.biblioteca.presentation.base.BaseViewModel

const val PARAM_USERID = "PARAM_USERID"
const val PARAM_USERNAME = "PARAM_USERNAME"
const val PARAM_EMAIL = "PARAM_EMAIL"
const val PARAM_PASSWORD = "PARAM_PASSWORD"
const val PARAM_TOKEN = "PARAM_TOKEN"

class AuthViewModel(
    private val authRepository : AuthRepository,
    private val paramRepository: ParamRepository
): BaseViewModel(){

    private val _auth = MutableLiveData<Auth>()
    val auth : LiveData<Auth> = _auth

    fun signIn(email : String, password: String){
        launch {
            val nAuth = authRepository.login(email, password)
            setParam(PARAM_USERID, nAuth.user.id.toString())
            setParam(PARAM_USERNAME, nAuth.user.name)
            setParam(PARAM_EMAIL, nAuth.user.email)
            setParam(PARAM_PASSWORD, password)
            setParam(PARAM_TOKEN, nAuth.token)
            _auth.postValue(nAuth)
        }
    }

    private fun setParam(key : String, value: String){
        var param = paramRepository.get(key)
        if (param == null){
            param = ParamEntity(key, value)
            paramRepository.add(param)
        }else{
            param.value = value
            paramRepository.update(param)
        }
    }

}