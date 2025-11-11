package com.example.listi.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.listi.repository.AuthRepository
import com.example.listi.ui.screens.LoginScreen
import com.example.listi.ui.screens.RegisterScreen
import com.example.listi.ui.theme.ListiTheme
import com.example.listi.viewModel.AuthViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val authRepository = AuthRepository(this)
        val viewModel = AuthViewModel(authRepository)

        setContent {
            ListiTheme {
                LoginScreen()
            }
        }
    }
}
