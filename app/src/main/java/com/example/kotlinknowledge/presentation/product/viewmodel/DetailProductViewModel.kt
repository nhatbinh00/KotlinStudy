package com.example.kotlinknowledge.presentation.product.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinknowledge.app.constant.AppKey
import com.example.kotlinknowledge.data.remote.requests.LoginRequest
import com.example.kotlinknowledge.domain.repositories.AuthenticationRepository
import com.example.kotlinknowledge.domain.repositories.ProductRepository
import com.example.kotlinknowledge.presentation.authentication.viewmodel.LoginUiState
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
class DetailProductViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {
    private val _uiStateFlow = MutableStateFlow<DetailProductUiState>(DetailProductUiState.Initial)
    internal val uiStateFlow: StateFlow<DetailProductUiState> = _uiStateFlow.asStateFlow()

    private inline fun emitState(value: (DetailProductUiState) -> DetailProductUiState) = _uiStateFlow.update(value)

    fun getDetailProduct(productId: String) {
        // make uiState loading
        emitState {
            DetailProductUiState.Loading
        }

        viewModelScope.launch {
            repository.getDetailProduct(productId)
                .fold(
                    success = {
                        val result = it
                        emitState {
                            DetailProductUiState.Succeeded(
                                product = result
                            )
                        }
                    },
                    failure = {
                        val error = it
                        emitState {
                            DetailProductUiState.Failure(error)
                        }
                    },
                )
        }
    }

    
}