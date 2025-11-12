package com.example.listi.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.listi.ui.screens.auth.LoginScreen
import com.example.listi.ui.screens.products.ProductsScreen
import com.example.listi.ui.screens.auth.ProfileScreen
import com.example.listi.ui.screens.auth.RegisterScreen
import com.example.listi.ui.screens.shoppingLists.ShoppingListsScreen
import com.example.listi.ui.screens.auth.AuthViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    modifier: Modifier = Modifier
) {

    val destination =
        if(authViewModel.uiState.isLogged) {
            ROUTE_LISTS
        } else {
            ROUTE_LOGIN
        }

    NavHost(
        navController = navController,
        startDestination = destination,
        modifier = modifier
    ) {
        composable(ROUTE_LISTS) { ShoppingListsScreen() }
        composable(ROUTE_PRODUCTS) { ProductsScreen() }
        composable(ROUTE_PROFILE) { ProfileScreen() }
        composable(ROUTE_LOGIN) { LoginScreen(authViewModel) }
    }
}
