package com.example.listi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.listi.ui.components.AppNav
import com.example.listi.ui.components.Destination
import com.example.listi.ui.components.NavigationBarExample
import com.example.listi.ui.components.TopAppBar
import com.example.listi.ui.screens.shoppingLists.ShoppingLists
import com.example.listi.ui.theme.ListiTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ListiTheme {
                Scaffold(
                    topBar = { TopAppBar() },
                    bottomBar = { NavigationBarExample() } // TODO: Remove this once we get the actual nav thingy
                ) { innerPadding ->
                    ShoppingLists(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview(modifier: Modifier = Modifier) {

    val navController = rememberNavController()
    val startDestination = Destination.LISTS
    var selectedDestination by rememberSaveable { mutableIntStateOf(startDestination.ordinal) }

    ListiTheme {
        Scaffold(
            modifier = modifier,
            topBar = { TopAppBar() },
            bottomBar = {
                NavigationBar(windowInsets = NavigationBarDefaults.windowInsets) {
                    Destination.entries.forEachIndexed { index, destination ->
                        NavigationBarItem(
                            selected = selectedDestination == index,
                            onClick = {
                                navController.navigate(route = destination.route)
                                selectedDestination = index
                            },
                            icon = {
                                Icon(
                                    destination.icon,
                                    contentDescription = destination.contentDescription
                                )
                            },
                            label = { Text(destination.label) }
                        )
                    }
                }
            }
        ) { contentPadding ->
            AppNav(navController, startDestination, modifier = Modifier.padding(contentPadding))
        }
    }
}