package br.com.biblioteca.presentation.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.biblioteca.domain.entity.validation.Error
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.lang.Exception

abstract class BaseViewModel : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _haveError = MutableLiveData<Boolean>()
    val haveError: LiveData<Boolean> = _haveError

    private val _error = MutableLiveData<Error?>()
    val error: LiveData<Error?> = _error

    fun launch(block: suspend () -> Unit){
        _loading.postValue(true)
        viewModelScope.launch {
            try {
                block()
                _haveError.postValue(false)
                _error.postValue(null)
            }catch (ex : HttpException){
                ex.printStackTrace()
                if (ex.code() != 400){
                    _haveError.postValue(true)
                }
                _error.postValue(Error(ex.code(), ex.message()))
            }catch (ex: Exception){
                ex.printStackTrace()
                _haveError.postValue(true)
                _error.postValue(Error(500, ex.message.toString()))
            }finally {
                _loading.postValue(false)
            }
        }
    }

    fun clearError() {
        _error.postValue(null)
    }

}