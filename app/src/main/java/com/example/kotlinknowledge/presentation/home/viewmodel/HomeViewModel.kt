package com.example.kotlinknowledge.presentation.home.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinknowledge.domain.model.CategoriesModel
import com.example.kotlinknowledge.domain.model.ProductsModel
import com.example.kotlinknowledge.domain.repositories.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {
    private val _featureProducts = MutableLiveData<ProductsModel>()
    val featureProducts: LiveData<ProductsModel> = _featureProducts

    private val _categories = MutableLiveData<CategoriesModel>()
    val categories: LiveData<CategoriesModel> = _categories

    fun getFeatureProducts(limit: Int = 10, skip: Int = 10) {
        viewModelScope.launch {
            try {
                val response = repository.getProducts(limit.toString(), skip.toString())
                _featureProducts.value = response
            } catch (e: Exception) {
                Log.d("ErrorTag", "${e.message}")
            }
        }
    }

    fun getCategories() {
        viewModelScope.launch {
            try {
                val response = repository.getCategories()
                _categories.value = response
            } catch (e: Exception) {
                Log.d("ErrorTag", "${e.message}")
            }
        }
    }
}