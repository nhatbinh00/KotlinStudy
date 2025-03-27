package com.example.kotlinknowledge.presentation.home.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.example.kotlinknowledge.app.theme.AppColors
import com.example.kotlinknowledge.app.theme.AppStyle
import com.example.kotlinknowledge.domain.model.ProductsModel.Product
import com.example.kotlinknowledge.presentation.home.viewmodel.HomeViewModel

@Composable
fun FeatureProducts(homeViewModel: HomeViewModel,goToDetailProductScreen: (String)-> Unit) {
    Column {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Feature Products",
                style = AppStyle.appFont.bold(20.sp),
            )
            Text(
                text = "Show all",
                style = AppStyle.appFont.regular(14.sp),
            )
        }
        ListHorizontal(homeViewModel,goToDetailProductScreen)
    }
}

@Composable
fun ListHorizontal(homeViewModel: HomeViewModel,goToDetailProductScreen: (String)-> Unit) {
    val featureProducts by homeViewModel.featureProducts.observeAsState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = AppStyle.appPadding.xxsm, bottom = AppStyle.appPadding.xxsm)
    ) {
        if (featureProducts?.products.isNullOrEmpty()) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.width(32.dp),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            }
        } else {
            featureProducts?.let {
                LazyRow {
                    items(featureProducts!!.products!!) {
                        it?.let {
                            SweaterCard(it,goToDetailProductScreen)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SweaterCard(product: Product,onClick: (String)-> Unit) {
    Column(
        modifier = Modifier.padding(end = AppStyle.appPadding.xxsm).clickable {
            onClick(product.id.toString())
        },
        horizontalAlignment = Alignment.Start,
    ) {
        AsyncImage(
            modifier = Modifier
                .size(width = 126.dp, height = 172.dp)
                .padding(bottom = AppStyle.appPadding.xxxs)
                .border(
                    width = 0.7.dp,
                    color = AppStyle.appColor.kGreyBackground,
                    shape = RoundedCornerShape(16.dp)
                ),
            model = ImageRequest.Builder(LocalContext.current)
                .data(product.thumbnail)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Fit,
        )
        Column(
            modifier = Modifier.width(126.dp)
        ) {
            Text(
                text = product.title ?: "",
                style = AppStyle.appFont.regular(12.sp),
                color = Color.Black,
                textAlign = TextAlign.Start,
                maxLines = 1,
            )
            Text(
                text = "$ ${product.price}",
                style = AppStyle.appFont.bold(16.sp),
                color = Color.Black,
                textAlign = TextAlign.Start,
                maxLines = 1,
            )
        }
    }
}