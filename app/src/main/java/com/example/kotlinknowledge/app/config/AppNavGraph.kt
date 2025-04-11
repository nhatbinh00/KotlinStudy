package com.example.kotlinknowledge.app.config

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.kotlinknowledge.app.config.model.ScreenRoute
import com.example.kotlinknowledge.presentation.BottomNavigationView
import com.example.kotlinknowledge.presentation.authentication.LoginScreen
import com.example.kotlinknowledge.presentation.discover.SearchScreen
import com.example.kotlinknowledge.presentation.product.DetailProductScreen

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        route = "ROOT_GRAPH_ROUTE",
        startDestination = "MAIN_GRAPH_ROUTE",
    ) {
        navigation(
            route = "MAIN_GRAPH_ROUTE",
            startDestination = ScreenRoute.Login.route,
        ) {
            composable(route = ScreenRoute.Login.route) {
                LoginScreen {
                    navController.navigate(ScreenRoute.BottomNavigationRoute.route) {
                        popUpTo(ScreenRoute.Login.route) {
                            inclusive = true
                        }
                    }
                }
            }
            composable(route = ScreenRoute.BottomNavigationRoute.route) {
                BottomNavigationView(
                    goToSearchScreen = {
                        navController.navigate(ScreenRoute.Search.route)
                    },
                    goToDetailProductScreen = { productId ->
                        navController.navigate(
                            ScreenRoute.DetailProduct.route.replace(
                                oldValue = "{productId}",
                                newValue = productId
                            )
                        )
                    }
                )
            }
            composable(route = ScreenRoute.DetailProduct.route) { navBackStackEntry ->
                val productId = navBackStackEntry.arguments?.getString("productId")
                /* We check if it's not null */
                productId?.let {
                    DetailProductScreen(productId = it, navController = navController)
                }
            }
        }
        composable(route = ScreenRoute.Search.route) {
            SearchScreen()
        }
    }
}


