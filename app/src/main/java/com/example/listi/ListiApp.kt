package com.example.listi

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import com.example.listi.ui.navigation.AppNavGraph
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavOptions
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.example.listi.ui.components.BottomBar
import com.example.listi.ui.navigation.AppDestinations
import com.example.listi.ui.navigation.Lists
import com.example.listi.ui.theme.ListiTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ListiApp() {
    ListiTheme {
        val navController = rememberNavController()
        val currentRoute by rememberSaveable { mutableStateOf(AppDestinations.LISTS) }
        Scaffold(
            bottomBar = {
                BottomBar(
                    currentRoute = currentRoute
                ) { route ->
                    var navOptions: NavOptions? = null
                    if (route == Lists) {
                        navOptions = navOptions {
                            popUpTo<Lists> { inclusive = true }
                        }
                    }

                    /*
                    navOptions = navOptions {
                        popUpTo<T> { saveState = true }
                            restoreState = true
                        }
                    */

                    navController.navigate(
                        route = route,
                        navOptions = navOptions)
                }
            }
        ) {
            AppNavGraph(navController = navController)
        }
    }
}


@Preview(showBackground = true, showSystemUi = true, device = "spec:width=411dp, height=891dp")
@Composable
fun ListiAppPreview() {
        ListiApp()
}