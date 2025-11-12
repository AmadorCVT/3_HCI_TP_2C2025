package com.example.listi.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.example.listi.ui.navigation.AppDestinations
import com.example.listi.ui.navigation.ROUTE_LOGIN

@Composable
fun BottomBar(
    currentRoute: String,
    onNavigateToRoute: (String) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary
    ) {
        AppDestinations.entries.forEach { item ->

            // Ignorar las secciones de autenticacion
            if (item.route != ROUTE_LOGIN)
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
