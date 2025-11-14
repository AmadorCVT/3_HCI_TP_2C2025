package com.example.listi.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.listi.ui.screens.auth.LoginScreen
import com.example.listi.ui.screens.products.ProductsScreen
import com.example.listi.ui.screens.auth.ProfileScreen
import com.example.listi.ui.screens.auth.RegisterScreen
import com.example.listi.ui.screens.shoppingLists.ShoppingListsScreen
import com.example.listi.ui.screens.auth.AuthViewModel
import com.example.listi.ui.screens.auth.VerifyAccountScreen
import com.example.listi.ui.screens.friends.FriendsScreen
import com.example.listi.ui.screens.shoppingLists.ShoppingListDetailsScreen
import com.example.listi.ui.screens.shoppingLists.ShoppingListsViewModel
import com.example.listi.ui.screens.shoppingLists.ShoppingListsViewModelFactory

object Constants {
    const val LIST_ID_ARG = "LIST_ID"
}

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

        // Para que compartan el mismo view model
        navigation(startDestination = ROUTE_LISTS, route = "lists_root") {
            composable(ROUTE_LISTS) {
                val backStackEntry = navController.getBackStackEntry("lists_root")
                val parentEntry = remember { backStackEntry }
                val vm: ShoppingListsViewModel =
                    viewModel(parentEntry, factory = ShoppingListsViewModelFactory())
                ShoppingListsScreen(
                    shoppingListViewModel = vm,
                    onNavigateToDetails = { listId ->
                        navController.navigate("ROUTE_LIST_DETAILS/$listId")
                    }
                )
            }
            composable("ROUTE_LIST_DETAILS/{${Constants.LIST_ID_ARG}}") { entry ->
                val listId = entry.arguments?.getInt(Constants.LIST_ID_ARG) ?: 0

                val backStackEntry = navController.getBackStackEntry("lists_root")
                val parentEntry = remember { backStackEntry }
                val vm: ShoppingListsViewModel =
                    viewModel(parentEntry, factory = ShoppingListsViewModelFactory())
                ShoppingListDetailsScreen(modifier,
                    shoppingListViewModel = vm,
                    listId = listId)
            }
        }

        composable(ROUTE_PRODUCTS) { ProductsScreen() }
        composable(ROUTE_FRIENDS) {
            FriendsScreen(
                authViewModel = authViewModel
        ) }
        composable(ROUTE_PROFILE) { ProfileScreen() }
        composable(ROUTE_PROFILE) { ProfileScreen(authViewModel = authViewModel) }
        composable(ROUTE_LOGIN) {
            LoginScreen(
                authViewModel = authViewModel,
                onCreateAccountClick = { navController.navigate(ROUTE_REGISTER) },
                onForgotPasswordClick = {  }
            )
        }
        composable(ROUTE_REGISTER) {
            RegisterScreen(
                authViewModel = authViewModel,
                goLogin = { navController.navigate(ROUTE_LOGIN) },
                goVerifyAccount = {navController.navigate(ROUTE_VERIFY)}
            )
        }
        composable(ROUTE_VERIFY){
            VerifyAccountScreen(authViewModel = authViewModel,
                goLogin = { navController.navigate(ROUTE_LOGIN) })
        }
    }
}
