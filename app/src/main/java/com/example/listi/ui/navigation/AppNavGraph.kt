package com.example.listi.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.listi.ui.screens.LoginScreen
import com.example.listi.ui.screens.ProductsScreen
import com.example.listi.ui.screens.ProfileScreen
import com.example.listi.ui.screens.RegisterScreen
import com.example.listi.ui.screens.ShoppingListsScreen
import com.example.listi.viewModel.AuthViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = ROUTE_LISTS,
        modifier = modifier
    ) {
        composable(ROUTE_LISTS) { ShoppingListsScreen() }
        composable(ROUTE_PRODUCTS) { ProductsScreen() }
        composable(ROUTE_PROFILE) { ProfileScreen() }
        composable(ROUTE_REGISTER) {
            RegisterScreen(
                onRegisterClick = { first, last, email, pass ->
                    authViewModel.register(first, last, email, pass)
                },
                onGoLoginClick = { navController.navigate(ROUTE_LOGIN) },
                onVerifyClick = { /* navegar a verify */ }
            )
        }
        composable(ROUTE_LOGIN) { LoginScreen() }
    }
}
