package com.example.kotlinknowledge.presentation.authentication.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinknowledge.app.constant.AppKey
import com.example.kotlinknowledge.data.remote.requests.LoginRequest
import com.example.kotlinknowledge.domain.repositories.AuthenticationRepository
import com.example.kotlinknowledge.ulti.SharedPrefs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import com.github.michaelbull.result.fold
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthenticationRepository
) : ViewModel() {
    private val _uiStateFlow = MutableStateFlow<LoginUiState>(LoginUiState.Initial)
    internal val uiStateFlow: StateFlow<LoginUiState> = _uiStateFlow.asStateFlow()

    private inline fun emitState(value: (LoginUiState) -> LoginUiState) = _uiStateFlow.update(value)

    init {
        uiStateFlow.onEach {
            when (it) {
                is LoginUiState.Failure -> Log.e(AppKey.appTag, "LoginUiState.Failure")
                is LoginUiState.Initial -> Log.e(AppKey.appTag, "LoginUiState.Initial")
                is LoginUiState.Loading -> Log.e(AppKey.appTag, "LoginUiState.Loading")
                is LoginUiState.Succeeded -> Log.e(AppKey.appTag, "LoginUiState.Succeeded")
            }
        }.launchIn(viewModelScope)
    }

    fun login(username: String, password: String) {
        // make uiState loading
        emitState {
            LoginUiState.Loading
        }

        viewModelScope.launch {
            repository.login(LoginRequest(username, password))
                .fold(
                    success = {
                        val result = it
                        //save token Authorization to Local
                        saveToken(result.accessToken)
                        emitState {
                            LoginUiState.Succeeded(
                                loginSessionData = result
                            )
                        }
                    },
                    failure = {
                        val error = it
                        emitState {
                            LoginUiState.Failure(error)
                        }
                    },
                )
        }
    }

    private fun saveToken(token: String) {
        SharedPrefs.put(AppKey.token, token)
    }
}