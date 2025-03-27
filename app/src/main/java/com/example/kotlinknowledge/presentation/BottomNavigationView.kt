package com.example.kotlinknowledge.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kotlinknowledge.app.config.model.ScreenRoute
import com.example.kotlinknowledge.app.theme.AppStyle
import com.example.kotlinknowledge.presentation.cart.CartScreen
import com.example.kotlinknowledge.presentation.home.HomeScreen
import com.example.kotlinknowledge.presentation.profile.ProfileScreen
import com.example.kotlinknowledge.presentation.discover.DiscoverScreen


sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    data object Home : BottomNavItem(ScreenRoute.Home.route, Icons.Default.Home, "Home")
    data object Search : BottomNavItem(ScreenRoute.Discover.route, Icons.Default.Search, "Search")
    data object Cart : BottomNavItem(ScreenRoute.Cart.route, Icons.Default.ShoppingCart, "Cart")
    data object Profile : BottomNavItem(ScreenRoute.Profile.route, Icons.Default.Person, "Profile")
}

@Composable
fun BottomNavigationView(
    goToSearchScreen: ()-> Unit,
    goToDetailProductScreen: (String)-> Unit,
) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomBar(
                navController = navController,
                modifier = Modifier
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier.padding(paddingValues)
        ) {
            NavHost(navController, startDestination = ScreenRoute.Home.route) {
                composable(ScreenRoute.Home.route) {
                    HomeScreen(goToDetailProductScreen = goToDetailProductScreen)
                }
                composable(ScreenRoute.Discover.route) {
                    DiscoverScreen(goToSearchScreen = goToSearchScreen)
                }
                composable(ScreenRoute.Cart.route) {
                    CartScreen(name = "CartScreen")
                }
                composable(ScreenRoute.Profile.route) {
                    ProfileScreen(name = "Profile")
                }
            }
        }
    }
}

@Composable
fun BottomBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {

    val screens = listOf(
        BottomNavItem.Home,
        BottomNavItem.Search,
        BottomNavItem.Cart,
        BottomNavItem.Profile,
    )

    NavigationBar(
        modifier = modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(
                    topStart = AppStyle.appPadding.xxsm,
                    topEnd = AppStyle.appPadding.xxsm,
                )
            ).background(
                color = Color.Yellow,
                shape = RoundedCornerShape(
                    topStart = AppStyle.appPadding.xxsm,
                    topEnd = AppStyle.appPadding.xxsm,
                )
            ),
        containerColor = Color.White,
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        screens.forEach { screen ->
            NavigationBarItem(
                label = {
                    Text(text = screen.label)
                },
                icon = {
                    Icon(imageVector = screen.icon, contentDescription = "")
                },
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    unselectedTextColor = Color.Gray,
                    selectedTextColor = Color.Black,
                    selectedIconColor = Color.Black,
                    unselectedIconColor = Color.Black,
                    indicatorColor = Color.White
                ),
            )
        }
    }
}

