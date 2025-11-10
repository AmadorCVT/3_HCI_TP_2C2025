package com.example.listi.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.example.listi.ui.navigation.AppDestinations

@Composable
fun BottomBar(
    currentRoute: Any,
    onNavigateToRoute: (Any) -> Unit
) {
    NavigationBar (
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary
    ) {
        AppDestinations.entries.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        ImageVector.vectorResource(item.icon),
                        contentDescription = stringResource(item.contentDescription)
                    )
                },
                label = { Text(text = stringResource(item.label)) },
                alwaysShowLabel = true,
                selected = currentRoute == item.route,
                onClick = { onNavigateToRoute(item.route) }
            )
        }
    }
}