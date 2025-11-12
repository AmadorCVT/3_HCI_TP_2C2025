package com.example.listi

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavOptions
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navOptions
import com.example.listi.network.RetrofitInstance
import com.example.listi.repository.AuthRepository
import com.example.listi.ui.components.BottomBar
import com.example.listi.ui.navigation.AppNavGraph
import com.example.listi.ui.navigation.ROUTE_LISTS
import com.example.listi.ui.theme.ListiTheme
import com.example.listi.ui.screens.auth.AuthViewModel
import com.example.listi.ui.screens.auth.AuthViewModelFactory

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ListiApp(
    context: Context
) {

    // Instancia el repositorio y el ViewModel usando la factory
    val authRepository = AuthRepository(context)
    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(authRepository)
    )

    ListiTheme {
        val navController = rememberNavController()

        // Backstack entry -> ruta actual (String)
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route ?: ROUTE_LISTS

        // ViewModel + Repository
        val authRepository = AuthRepository(navController.context)
        val authViewModel: AuthViewModel = viewModel(
            factory = AuthViewModelFactory(authRepository)
        )

        Scaffold(
            bottomBar = {
                BottomBar(
                    currentRoute = currentRoute,
                    onNavigateToRoute = { route ->
                        var options: NavOptions? = null
                        if (route == ROUTE_LISTS) {
                            options = navOptions {
                                popUpTo(ROUTE_LISTS) { inclusive = true }
                            }
                        }
                        navController.navigate(route, options)
                    }
                )
            }
        ) { innerPadding ->
            AppNavGraph(
                navController = navController,
                viewModel = authViewModel,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "ViewModelConstructorInComposable")
@Preview(showBackground = true, showSystemUi = true, device = "spec:width=411dp,height=891dp")
@Composable
fun ListiAppPreview() {
    ListiApp(LocalContext.current)
}
