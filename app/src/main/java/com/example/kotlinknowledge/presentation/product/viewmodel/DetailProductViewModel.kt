package com.example.kotlinknowledge.presentation.product.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinknowledge.MainApplication
import com.example.kotlinknowledge.app.constant.AppKey
import com.example.kotlinknowledge.domain.model.AppError
import com.example.kotlinknowledge.domain.model.FavoriteProduct
import com.example.kotlinknowledge.domain.repositories.ProductRepository
import com.example.kotlinknowledge.ulti.SharedPrefs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import com.github.michaelbull.result.fold
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class DetailProductViewModel @Inject constructor(
    private val repository: ProductRepository,
) : ViewModel() {
    private lateinit var favoritesProduct: List<FavoriteProduct>
    private val _uiStateFlow = MutableStateFlow<DetailProductUiState>(DetailProductUiState.Initial)
    internal val uiStateFlow: StateFlow<DetailProductUiState> = _uiStateFlow.asStateFlow()

    private inline fun emitState(value: (DetailProductUiState) -> DetailProductUiState) =
        _uiStateFlow.update(value)

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                favoritesProduct = MainApplication.db.favoriteProductDao().getAll()
            }
        }
    }

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
                                product = result.copy(
                                    isLiked = checkExistInFavorite(result.id.toInt())
                                )
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

    fun addToFavorite(
        onAddToFavoriteSuccess: () -> Unit,
        onRemoveToFavoriteSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        viewModelScope.launch {
            if (_uiStateFlow.value is DetailProductUiState.Succeeded) {
                val product = (_uiStateFlow.value as DetailProductUiState.Succeeded).product

                if(product.isLiked == true){
                    val result = repository.removeFavorite(
                        FavoriteProduct(
                            id = product.id.toInt(),
                            title = product.title,
                            userId = SharedPrefs.get(AppKey.USER_ID, AppKey.emptyString)
                        ),
                    )

                    if (result) {
                        onRemoveToFavoriteSuccess()
                    } else {
                        onError()
                    }
                }
                else{
                    val result = repository.addToFavorite(
                        FavoriteProduct(
                            id = product.id.toInt(),
                            title = product.title,
                            userId = SharedPrefs.get(AppKey.USER_ID, AppKey.emptyString)
                        ),
                    )

                    if (result) {
                        onAddToFavoriteSuccess()
                    } else {
                        onError()
                    }
                }

                emitState {
                    DetailProductUiState.Succeeded(
                        product = product.copy(
                            isLiked = !product.isLiked!!
                        )
                    )
                }

            }
        }
    }

    fun addToCart(
        quantity: Int,
        onAddToCartSuccess: () -> Unit,
        onError: (AppError) -> Unit
    ) {
        viewModelScope.launch {
            if (_uiStateFlow.value is DetailProductUiState.Succeeded) {
                val product = (_uiStateFlow.value as DetailProductUiState.Succeeded).product
                val userId = SharedPrefs.get(AppKey.USER_ID, AppKey.emptyString).toInt()

                repository.addToCart(userId, product.id.toInt(), quantity)
                    .collectLatest { result ->
                        result.fold(
                            success = {
                                onAddToCartSuccess()
                            },
                            failure = { error ->
                                onError(AppError.ApiError(error.message ?: "Failed to add to cart"))
                            }
                        )
                    }
            }
        }
    }

    private suspend fun checkExistInFavorite(productId: Int): Boolean = withContext(Dispatchers.IO){
        // check if product is already in favorites
        val exist = favoritesProduct.any {
            it.id == productId && it.userId == SharedPrefs.get(
                AppKey.USER_ID,
                AppKey.emptyString
            )
        }

        return@withContext exist
    }

}