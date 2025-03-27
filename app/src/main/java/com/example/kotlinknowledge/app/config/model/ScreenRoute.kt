package com.example.kotlinknowledge.app.config.model

sealed class ScreenRoute(val route: String) {
    data object Login: ScreenRoute("home_screen")
    data object Home: ScreenRoute("login_screen")
    data object Profile: ScreenRoute("profile_screen")
    data object Discover: ScreenRoute("discover_screen")
    data object Cart: ScreenRoute("cart_screen")
    data object BottomNavigationRoute: ScreenRoute("bottom_nav_view")
    data object Search: ScreenRoute("search_screen")
    data object DetailProduct: ScreenRoute("detail_product_screen/{productId}")
}