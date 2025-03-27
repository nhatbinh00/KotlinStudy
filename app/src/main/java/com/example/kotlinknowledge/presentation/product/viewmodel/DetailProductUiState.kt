package com.example.kotlinknowledge.presentation.product.viewmodel

import androidx.compose.runtime.Immutable
import com.example.kotlinknowledge.domain.model.AppError
import com.example.kotlinknowledge.domain.model.DetailProductModel
@Immutable
sealed interface DetailProductUiState {
    @Immutable
    data object Initial : DetailProductUiState

    @Immutable
    data object Loading : DetailProductUiState

    @Immutable
    data class Succeeded(
        val product: DetailProductModel,
    ) : DetailProductUiState

    @Immutable
    data class Failure(
        val error: AppError,
    ) : DetailProductUiState
}