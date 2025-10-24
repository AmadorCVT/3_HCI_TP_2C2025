package com.example.listi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.listi.ui.components.CenterAlignedTopAppBar
import com.example.listi.ui.screens.shoppingLists.ShoppingLists
import com.example.listi.ui.theme.ListiTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ListiTheme {
                Scaffold(
                    topBar = { CenterAlignedTopAppBar() }
                ) { innerPadding ->
                    ShoppingLists(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    ListiTheme {
        Scaffold(
            topBar = { CenterAlignedTopAppBar() }
        ) { innerPadding ->
            ShoppingLists(Modifier.padding(innerPadding))
        }
    }
}