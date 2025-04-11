package com.example.kotlinknowledge.presentation.product

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.rounded.HeartBroken
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.example.kotlinknowledge.R
import com.example.kotlinknowledge.app.theme.AppStyle
import com.example.kotlinknowledge.presentation.product.viewmodel.DetailProductUiState
import com.example.kotlinknowledge.presentation.product.viewmodel.DetailProductViewModel
import com.example.kotlinknowledge.presentation.widgets.LoadingBox
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import  com.example.kotlinknowledge.domain.model.DetailProductModel.Review
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalPagerApi::class)
@Composable
fun DetailProductScreen(
    modifier: Modifier = Modifier,
    productId: String,
    navController: NavHostController
) {
    val detailProductViewModel: DetailProductViewModel = hiltViewModel<DetailProductViewModel>()

    val uiState by detailProductViewModel.uiStateFlow.collectAsStateWithLifecycle()
    val pagerState: PagerState = remember { PagerState() }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        detailProductViewModel.getDetailProduct(productId)
    }

    LoadingBox(
        isLoading = uiState == DetailProductUiState.Loading,
    ) {
        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            containerColor = Color.White
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(color = Color.White)
            ) {
                if (uiState is DetailProductUiState.Succeeded) {
                    val product = (uiState as DetailProductUiState.Succeeded).product
                    Column {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            HorizontalPager(
                                count = product.images.size,
                                state = pagerState,
                            ) { page ->
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(product.images[page])
                                        .build(),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.height(200.dp)
                                )
                            }
                            OutlinedIconButton(
                                onClick = {
                                    detailProductViewModel.addToFavorite(
                                        onAddToFavoriteSuccess = {
                                            coroutineScope.launch {
                                                snackbarHostState.showSnackbar(
                                                    message = "Đã thêm sản phẩm vào mục Yêu Thích"
                                                )
                                            }
                                        },
                                        onRemoveToFavoriteSuccess = {
                                            coroutineScope.launch {
                                                snackbarHostState.showSnackbar(
                                                    message = "Đã xoá sản phẩm khỏi mục Yêu Thích"
                                                )
                                            }
                                        },
                                        onError = {
                                            coroutineScope.launch {
                                                snackbarHostState.showSnackbar(
                                                    message = "Đã có lỗi xãy ra!"
                                                )
                                            }
                                        }
                                    )
                                },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(16.dp)
                            ) {
                                Icon(
                                    //painter = painterResource(id = R.drawable.ic_heart),
                                    Icons.Rounded.HeartBroken,
                                    contentDescription = "Favorite",
                                    tint = if (product.isLiked == true) Color.Red else Color.Gray
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .shadow(
                                    8.dp,
                                    shape = RoundedCornerShape(12.dp),
                                )
                                .background(color = AppStyle.appColor.kWhite)
                                .fillMaxSize(),
                        ) {
                            Column() {
                                Column(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .weight(1f)
                                        .verticalScroll(rememberScrollState())
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = product.title,
                                            fontSize = 22.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "$${product.price}",
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.Black
                                        )
                                    }
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(top = 16.dp)
                                    ) {
                                        repeat(5) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.ic_star),
                                                contentDescription = "Star",
                                                tint = Color.Unspecified
                                            )
                                        }
                                        Text(
                                            text = "(${product.reviews.size})",
                                            fontSize = 14.sp,
                                            color = Color.Gray
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(16.dp))
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                    ) {
                                        Row(
                                            modifier = Modifier.weight(1f),
                                            verticalAlignment = Alignment.CenterVertically,
                                        ) {
                                            Text(text = "Color")
                                            Row {
                                                Spacer(modifier = Modifier.width(16.dp))
                                                listOf(
                                                    Color.Blue,
                                                    Color.Black,
                                                    Color.Red
                                                ).forEach { color ->
                                                    Box(
                                                        modifier = Modifier
                                                            .padding(4.dp)
                                                            .size(25.dp)
                                                            .clip(CircleShape)
                                                            .background(color)
                                                    )
                                                }
                                            }
                                        }

                                        Row(
                                            modifier = Modifier.weight(1f),
                                            verticalAlignment = Alignment.CenterVertically,
                                        ) {
                                            Spacer(modifier = Modifier.height(16.dp))
                                            Text(text = "Size")
                                            Row {
                                                Spacer(modifier = Modifier.width(16.dp))
                                                listOf("S", "M", "L").forEach { size ->
                                                    Box(
                                                        modifier = Modifier
                                                            .padding(4.dp)
                                                            .size(25.dp)
                                                            .clip(CircleShape)
                                                            .background(Color.Gray),
                                                        contentAlignment = Alignment.Center
                                                    ) {
                                                        Text(text = size)
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(text = "Description", fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = product.description,
                                        fontSize = 14.sp,
                                        color = Color.Gray
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(text = "Reviews", fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()

                                    ) {
                                        Text(
                                            text = "${product.rating} OUT OF 5",
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Column {
                                            Row {
                                                repeat(5) {
                                                    Icon(
                                                        painter = painterResource(id = R.drawable.ic_star),
                                                        contentDescription = "Star",
                                                        tint = Color.Unspecified
                                                    )
                                                }
                                            }
                                            Spacer(modifier = Modifier.height(6.dp))
                                            Text(text = "${product.reviews.size} ratings")
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(16.dp))
                                    ReviewAnalytics(product.reviews)
                                    Spacer(modifier = Modifier.height(16.dp))
                                    product.reviews.map {
                                        Reviews(it)
                                    }
                                }

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                ) {
                                    Button(
                                        onClick = {
                                            detailProductViewModel.addToCart(
                                                quantity = 1,
                                                onAddToCartSuccess = {
                                                    coroutineScope.launch {
                                                        snackbarHostState.showSnackbar(
                                                            message = "Đã thêm vào giỏ hàng thành công!"
                                                        )
                                                    }
                                                    navController.navigate("cart_screen")
                                                },
                                                onError = { error ->
                                                    coroutineScope.launch {
                                                        snackbarHostState.showSnackbar(
                                                            message = "Lỗi khi thêm vào giỏ hàng: ${error.message}"
                                                        )
                                                    }
                                                }
                                            )
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(50.dp)),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color.Black,
                                            contentColor = Color.White
                                        ),
                                        contentPadding = PaddingValues(
                                            horizontal = 25.dp,
                                            vertical = 15.dp
                                        )
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Outlined.ShoppingBag,
                                                contentDescription = "Add to cart",
                                                tint = Color.White,
                                                modifier = Modifier.size(20.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = "Add To Cart",
                                                style = TextStyle(
                                                    fontWeight = FontWeight.Medium,
                                                    fontSize = 15.sp
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Reviews(review: Review) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_star),
            contentDescription = "Star",
            tint = Color.Unspecified
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = review.reviewerName, fontWeight = FontWeight.Bold)
    }
    Spacer(modifier = Modifier.height(6.dp))
    Text(
        text = review.comment,
        fontSize = 14.sp,
        color = Color.Gray,
        modifier = Modifier.padding(bottom = 16.dp)
    )
}


@Composable
fun ReviewAnalytics(listReview: List<Review?>) {
    val listStar = mutableListOf(5, 4, 3, 2, 1)

    Column {
        listStar.map {
            val star = it
            val amountOfStar = listReview.filter { it -> it?.rating?.toInt() == star }
            val percentage = (amountOfStar.size.toFloat() / listReview.size.toFloat()) * 100
            Row(
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = it.toString())
                Icon(
                    painter = painterResource(id = R.drawable.ic_star),
                    contentDescription = "Star",
                    tint = Color.Unspecified,
                    modifier = Modifier.padding(horizontal = 6.dp)
                )
                LinearProgressIndicator(
                    progress = { percentage / 100 },
                    modifier = Modifier
                        .weight(1f)
                        .height(7.dp)
                        .clip(
                            RoundedCornerShape(10.dp)
                        ),
                    color = Color(0xff508A7B)
                )
                Spacer(Modifier.width(12.dp))
                Text(text = "${percentage.toInt()} %")
            }
        }
    }
}
