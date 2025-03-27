package com.example.kotlinknowledge.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.kotlinknowledge.presentation.home.widget.CategoriesWidget
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.kotlinknowledge.presentation.home.viewmodel.HomeViewModel
import com.example.kotlinknowledge.presentation.home.widget.Advertisement
import com.example.kotlinknowledge.presentation.home.widget.BannerSlider
import com.example.kotlinknowledge.presentation.home.widget.FeatureProducts
import com.example.kotlinknowledge.presentation.home.widget.HomeHeader
import com.example.kotlinknowledge.presentation.home.widget.Recommended
import com.example.kotlinknowledge.presentation.home.widget.TopCollection

@Composable
fun HomeScreen(modifier: Modifier = Modifier,goToDetailProductScreen: (String)-> Unit) {
    val homeViewModel: HomeViewModel = hiltViewModel<HomeViewModel>()

    LaunchedEffect(Unit) {
        homeViewModel.getCategories()
        homeViewModel.getFeatureProducts()
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(
                color = Color.White
            )
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
        ) {
            HomeHeader(modifier)
            CategoriesWidget(homeViewModel)
            BannerSlider()
            FeatureProducts(homeViewModel){
                goToDetailProductScreen(it)
            }
            Advertisement()
            Recommended(homeViewModel)
            TopCollection()
        }
    }
}



