package com.example.listi.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.listi.ui.screens.LoginScreen
import com.example.listi.ui.screens.ProductsScreen
import com.example.listi.ui.screens.ProfileScreen
import com.example.listi.ui.screens.RegisterScreen
import com.example.listi.ui.screens.ShoppingListsScreen

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Lists
    ) {
        composable<Lists> { ShoppingListsScreen() }
        composable<Products> { ProductsScreen() }
        composable<Profile> { ProfileScreen() }
        composable<Register> { RegisterScreen() }
        composable<Login> { LoginScreen() }
    }
}