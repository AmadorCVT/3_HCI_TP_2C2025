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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Surface
import androidx.compose.material3.MaterialTheme

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.Alignment
import com.example.listi.ui.navigation.Constants


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

        val context = LocalContext.current
        val authRepository = remember { AuthRepository(context) }

        // Chequear si el usuario esta loggeado
        var startDestination by remember { mutableStateOf<String?>(null) }

        val authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(authRepository))

        val isLogged by authViewModel.isLogged.collectAsState()
        val isLoading by authViewModel.isLoading.collectAsState()

        LaunchedEffect(Unit) {
            val savedToken = authRepository.getSavedToken()
            if (!savedToken.isNullOrBlank()) {
                authViewModel.loadProfile()
                startDestination = ROUTE_LISTS     // Loggeado
            } else {
                   startDestination = ROUTE_LOGIN     // Sin credenciales
            }
        }

        if (startDestination == null) {
            Box(Modifier.fillMaxSize(), Alignment.Center) { CircularProgressIndicator() }
            return@ListiTheme
        }

        // Borra todo el stack si se hizo logout
        LaunchedEffect(isLogged) {
            if (!isLoading && !isLogged) {
                navController.navigate(ROUTE_LOGIN) {
                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                }
            }
        }

        val isTablet = isTablet()
        val isLandscape = isLandscape()

        Scaffold(
            topBar = {
                if (!isTablet) {
                    if (currentRoute in noBarsRoutes) {
                        val topBarTitle = if (currentRoute.startsWith("list_details"))
                        {"Shopping List"} else {currentRoute }
                        AppTopBar(topBarTitle)
                    }
                }
            },
            bottomBar = {
                if (!isTablet) {   // celular -> bottombar
                    if (currentRoute in noBarsRoutes &&  currentRoute != "${Constants.ROUTE_LIST_DETAILS}/{${Constants.LIST_ID_ARG}}")
                        BottomBar(
                            currentRoute = currentRoute,
                            onNavigateToRoute = { route ->
                                navController.navigate(route)
                            }
                        )
                }
            },
            content = { innerPadding ->

                // Tablet horizontal
                if (isTablet && !isLandscape) {
                    if (currentRoute in noBarsRoutes) {
                        Row(modifier = Modifier.fillMaxSize()) {

                            Surface(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .width(80.dp),
                                tonalElevation = 2.dp,
                                color = MaterialTheme.colorScheme.surface
                            ) {
                                NavigationRailBar(
                                    currentRoute = currentRoute,
                                    onNavigateToRoute = { navController.navigate(it) }
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxSize()
                                    .padding(innerPadding)
                            ) {
                                AppNavGraph(
                                    navController = navController,
                                    authViewModel = authViewModel,
                                    startDestination = startDestination!!,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                        return@Scaffold
                    }
                }

                // Tablet vertical
                // Tablet horizontal
                if (isTablet && isLandscape) {
                    if (currentRoute in noBarsRoutes) {
                        Row(modifier = Modifier.fillMaxSize()) {

                            Surface(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .width(240.dp),
                                tonalElevation = 2.dp,
                                color = MaterialTheme.colorScheme.surface
                            ) {
                                NavigationDrawerBar(
                                    currentRoute = currentRoute,
                                    onNavigateToRoute = { navController.navigate(it) }
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxSize()
                                    .padding(innerPadding)
                            ) {
                                AppNavGraph(
                                    navController = navController,
                                    authViewModel = authViewModel,
                                    startDestination = startDestination!!,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                        return@Scaffold
                    }
                }


                // Celular
                Box(modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                ) {
                    AppNavGraph(
                        navController = navController,
                        authViewModel = authViewModel,
                        startDestination = startDestination!!,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        )
    }
}

private val noBarsRoutes = listOf(
    ROUTE_LISTS, ROUTE_PRODUCTS, ROUTE_FRIENDS, ROUTE_PROFILE, "${Constants.ROUTE_LIST_DETAILS}/{${Constants.LIST_ID_ARG}}"
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
@Preview(showBackground = true, showSystemUi = true, device = "spec:width=900dp,height=1400dp")
@Preview(showBackground = true, showSystemUi = true, device = "spec:width=900dp,height=1400dp, orientation=landscape")

@Composable
fun ListiAppPreview() {
    RetrofitInstance.init(
        context = LocalContext.current
    )
    ListiApp(context = LocalContext.current)
}
