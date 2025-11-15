package com.example.listi

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavOptions
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navOptions
import com.example.listi.network.RetrofitInstance
import com.example.listi.repository.AuthRepository
import com.example.listi.ui.components.AppTopBar
import com.example.listi.ui.components.BottomBar
import com.example.listi.ui.components.DrawerBar
import com.example.listi.ui.components.RailBar
import com.example.listi.ui.navigation.AppNavGraph
import com.example.listi.ui.navigation.ROUTE_FRIENDS
import com.example.listi.ui.navigation.ROUTE_LISTS
import com.example.listi.ui.navigation.ROUTE_LOGIN
import com.example.listi.ui.navigation.ROUTE_PASSWORD
import com.example.listi.ui.navigation.ROUTE_PASSWORD_CODE
import com.example.listi.ui.navigation.ROUTE_PRODUCTS
import com.example.listi.ui.navigation.ROUTE_PROFILE
import com.example.listi.ui.navigation.ROUTE_REGISTER
import com.example.listi.ui.navigation.ROUTE_VERIFY
import com.example.listi.ui.theme.ListiTheme
import com.example.listi.ui.screens.auth.AuthViewModel
import com.example.listi.ui.screens.auth.AuthViewModelFactory


@Composable
fun isTablet(): Boolean {
    val config = LocalConfiguration.current
    val smallestWidthDp = config.smallestScreenWidthDp
    return smallestWidthDp >= 600
}

@Composable
fun isLandscape(): Boolean {
    val orientation = LocalConfiguration.current.orientation
    return orientation == Configuration.ORIENTATION_LANDSCAPE
}

@Composable
fun ListiApp(
    context: Context
) {

    ListiTheme {

        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route ?: ROUTE_LISTS

        val authRepository = AuthRepository(context)
        val authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(authRepository))

        val isTablet = isTablet()
        val isLandscape = isLandscape()

        Scaffold(
            topBar = {
                if (!isTablet) {   // celular -> topbar
                    if (currentRoute in barsRoutes)
                        AppTopBar(currentRoute)
                }
            },
            bottomBar = {
                if (!isTablet) {   // celular -> bottombar
                    if (currentRoute in barsRoutes)
                        BottomBar(
                            currentRoute = currentRoute,
                            onNavigateToRoute = { route ->
                                navController.navigate(route)
                            }
                        )
                }
            },
            content = { innerPadding ->

                // TABLET LANDSCAPE → NavigationRail
                if (isTablet && !isLandscape) {
                    NavigationRailBar(
                        currentRoute = currentRoute,
                        onNavigateToRoute = { navController.navigate(it) }
                    )
                }

                // TABLET PORTRAIT → Navigation Drawer
                if (isTablet && isLandscape) {
                    NavigationDrawerBar(
                        currentRoute = currentRoute,
                        onNavigateToRoute = { navController.navigate(it) }
                    )
                }

                AppNavGraph(
                    navController = navController,
                    authViewModel = authViewModel,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        )
    }
}

private val barsRoutes = listOf(
    ROUTE_LISTS, ROUTE_PRODUCTS, ROUTE_FRIENDS, ROUTE_PROFILE
)

@Composable
fun NavigationRailBar(
    currentRoute: String,
    onNavigateToRoute: (String) -> Unit
) {
    RailBar(
        currentRoute = currentRoute,
        onNavigateToRoute = onNavigateToRoute
    )
}

@Composable
fun NavigationDrawerBar(
    currentRoute: String,
    onNavigateToRoute: (String) -> Unit
) {
    DrawerBar(
        currentRoute = currentRoute,
        onNavigateToRoute = onNavigateToRoute
    )
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "ViewModelConstructorInComposable")
@Preview(showBackground = true, showSystemUi = true, device = "spec:width=411dp,height=891dp")
@Composable
fun ListiAppPreview() {
    RetrofitInstance.init(
        context = LocalContext.current
    )
    ListiTheme {
        val navController = rememberNavController()
        Scaffold (
            topBar = { AppTopBar("Test") },
            bottomBar = { BottomBar(currentRoute = ROUTE_LISTS, onNavigateToRoute = {}) }
        ) {
            AppNavGraph(
                navController = navController,
                authViewModel = AuthViewModel(AuthRepository(navController.context))
            )
        }
    }
}
