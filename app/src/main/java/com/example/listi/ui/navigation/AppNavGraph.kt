package com.example.listi.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.listi.network.RetrofitInstance
import com.example.listi.repository.CategoryRepository
import com.example.listi.ui.screens.auth.LoginScreen
import com.example.listi.ui.screens.products.ProductsScreen
import com.example.listi.ui.screens.auth.ProfileScreen
import com.example.listi.ui.screens.auth.RegisterScreen
import com.example.listi.ui.screens.shoppingLists.ShoppingListsScreen
import com.example.listi.ui.screens.auth.AuthViewModel
import com.example.listi.ui.screens.auth.RecoverPasswordScreenCode
import com.example.listi.ui.screens.auth.ResetPasswordScreen
import com.example.listi.ui.screens.auth.VerifyAccountScreen
import com.example.listi.ui.screens.friends.FriendsScreen
import com.example.listi.ui.screens.products.CategoryViewModel
import com.example.listi.ui.screens.products.CategoryViewModelFactory
import com.example.listi.ui.screens.products.ProductViewModel
import com.example.listi.ui.screens.products.ProductViewModelFactory
import com.example.listi.ui.screens.shoppingLists.ShoppingListDetailsScreen
import com.example.listi.ui.screens.shoppingLists.ShoppingListItemsViewModel
import com.example.listi.ui.screens.shoppingLists.ShoppingListItemsViewModelFactory
import com.example.listi.ui.screens.shoppingLists.ShoppingListsViewModel
import com.example.listi.ui.screens.shoppingLists.ShoppingListsViewModelFactory

object Constants {
    const val ROUTE_LIST_DETAILS = "list_details"
    const val LIST_ID_ARG = "listId"
}

@Composable
fun AppNavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    modifier: Modifier = Modifier
) {

    // Crear los viewModels asi se pueden pasar entre las vistas que los requieren
    val productViewModel: ProductViewModel =
        viewModel(factory = ProductViewModelFactory())

    val categoryViewModel: CategoryViewModel =
        viewModel(factory = CategoryViewModelFactory(
            CategoryRepository(RetrofitInstance.categoryService)
        ))

    val shoppingListsViewModel: ShoppingListsViewModel =
        viewModel(factory = ShoppingListsViewModelFactory())

    val shoppingListItemsViewModel: ShoppingListItemsViewModel =
        viewModel(factory = ShoppingListItemsViewModelFactory())

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
        composable(ROUTE_LISTS) {
            ShoppingListsScreen(
                shoppingListViewModel = shoppingListsViewModel,
                onNavigateToDetails = { listId ->
                    navController.navigate("${Constants.ROUTE_LIST_DETAILS}/$listId")
                }
            )
        }

        composable(
            route = "${Constants.ROUTE_LIST_DETAILS}/{${Constants.LIST_ID_ARG}}",
            arguments = listOf(
                navArgument(Constants.LIST_ID_ARG) {
                    type = NavType.IntType
                }
            )) { entry ->
            val listId = entry.arguments!!.getInt(Constants.LIST_ID_ARG)
            ShoppingListDetailsScreen(
                modifier,
                shoppingListViewModel = shoppingListsViewModel,
                listId = listId
            )
        }

        composable(ROUTE_PRODUCTS) {
            ProductsScreen(
                viewModel = productViewModel,
                categoryViewModel = categoryViewModel
            ) }

        composable(ROUTE_FRIENDS) {
            FriendsScreen(
                authViewModel = authViewModel
        ) }

        // Perfil: pasar el authViewModel y el onChangePhoto que lanza el selector
        composable(ROUTE_PROFILE) {
            ProfileScreen(authViewModel = authViewModel)
        }

        composable(ROUTE_LOGIN) {
            LoginScreen(
                authViewModel = authViewModel,
                onCreateAccountClick = { navController.navigate(ROUTE_REGISTER) },
                onForgotPasswordClick = { navController.navigate(ROUTE_PASSWORD_CODE) }
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
        composable(ROUTE_PASSWORD){
            ResetPasswordScreen(authViewModel = authViewModel,
                goLogin = { navController.navigate(ROUTE_LOGIN) })
        }
        composable(ROUTE_PASSWORD_CODE){
            RecoverPasswordScreenCode(authViewModel = authViewModel,
                goRestorePassword = { navController.navigate(ROUTE_PASSWORD) })
        }
    }
}
