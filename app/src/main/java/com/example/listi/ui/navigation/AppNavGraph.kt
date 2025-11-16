package com.example.listi.ui.navigation

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import androidx.compose.ui.res.stringResource
import com.example.listi.R
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
    startDestination: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var lastBackPressed by remember { mutableStateOf(0L) }
    val exitRoutes = listOf(ROUTE_LISTS, ROUTE_PRODUCTS, ROUTE_FRIENDS, ROUTE_PROFILE)

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

    NavHost(
        navController = navController,
        startDestination = startDestination,
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
                productViewModel = productViewModel,
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
                authViewModel = authViewModel,
                shoppingListViewModel = shoppingListsViewModel
        ) }

        // Perfil: pasar el authViewModel y el onChangePhoto que lanza el selector
        composable(ROUTE_PROFILE) {
            ProfileScreen(authViewModel = authViewModel)
        }

        composable(ROUTE_LOGIN) {
            val uiState = authViewModel.uiState

            if (uiState.isLogged) {
                LaunchedEffect(Unit) {
                    navController.navigate(ROUTE_LISTS) {
                        popUpTo(ROUTE_LOGIN) { inclusive = true }
                    }
                }

                Box(modifier = Modifier.fillMaxSize())

                return@composable
            }

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


    val pressToExit = stringResource(R.string.press_to_exit)

    // Aca usamos un toast porque no hay manera sencilla de incorporar un scaffold para el snackbar
    BackHandler(enabled = true) {

        val currentRoute = navController.currentBackStackEntry?.destination?.route

        if (currentRoute in exitRoutes) {

            val now = System.currentTimeMillis()

            if (now - lastBackPressed < 2000) {
                (context as Activity).finish()
            } else {
                Toast.makeText(context, pressToExit, Toast.LENGTH_SHORT).show()
                lastBackPressed = now
            }

        } else {
            navController.popBackStack()
        }
    }
}
