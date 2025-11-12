package com.example.listi.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.listi.repository.AuthRepository
import com.example.listi.ui.navigation.AppNavGraph
import com.example.listi.ui.theme.ListiTheme
import com.example.listi.ui.screens.auth.AuthViewModel
import com.example.listi.ui.screens.auth.AuthViewModelFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ListiTheme {
                val navController = rememberNavController()

                // Instancia el repositorio y el ViewModel usando la factory
                val authRepository = AuthRepository(this)
                val authViewModel: AuthViewModel = viewModel(
                    factory = AuthViewModelFactory(authRepository)
                )

                // Le pasás el viewmodel al NavGraph
                AppNavGraph(
                    navController = navController,
                    viewModel = authViewModel
                )
            }
        }
    }
}
