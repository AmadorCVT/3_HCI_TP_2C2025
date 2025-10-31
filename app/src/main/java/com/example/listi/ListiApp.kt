package com.example.listi

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.listi.ui.components.AppTopBar
import com.example.listi.ui.navigation.AppDestinations
import com.example.listi.ui.screens.ProductsScreen
import com.example.listi.ui.screens.ProfileScreen
import com.example.listi.ui.screens.ShoppingListsScreen
import com.example.listi.ui.theme.ListiTheme

@Composable
fun ListiApp(modifier: Modifier = Modifier) {
    ListiTheme {
        val adaptiveInfo = currentWindowAdaptiveInfo()

        var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.LISTS) }

        NavigationSuiteScaffold(
            navigationSuiteItems = {
                AppDestinations.entries.forEach {
                    item(
                        icon = {
                            Icon(
                                painterResource(it.icon),
                                contentDescription = stringResource(it.contentDescription),
                                modifier = Modifier.size(dimensionResource(R.dimen.medium_icon_size ))
                            )
                        },
                        label = { Text(stringResource(it.label)) },
                        selected = it == currentDestination,
                        onClick = { currentDestination = it },
                    )
                }
            },
            layoutType = NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(adaptiveInfo),
            navigationSuiteColors = NavigationSuiteDefaults.colors(
                navigationBarContainerColor = MaterialTheme.colorScheme.primaryContainer,
                navigationBarContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            ),
        ) {
            when(currentDestination) {
                AppDestinations.LISTS -> ShoppingListsScreen()
                AppDestinations.PRODUCTS -> ProductsScreen()
                AppDestinations.PROFILE -> ProfileScreen()
            }
        }
    }
}


@Preview(device = "spec:width=411dp,height=891dp")
@Composable
fun ListiAppPreview() {
        ListiApp()
}