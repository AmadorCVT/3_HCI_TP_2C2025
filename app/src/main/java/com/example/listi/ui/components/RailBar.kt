package com.example.listi.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.listi.ui.navigation.AppDestinations
import com.example.listi.ui.navigation.ROUTE_LOGIN


@Composable
fun RailBar(
    currentRoute: String,
    onNavigateToRoute: (String) -> Unit
) {
    NavigationRail(
        header = {
            Column(
                modifier = Modifier.padding(vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "User Profile",
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Username") // Placeholder
            }
        }
    ) {
        AppDestinations.entries.forEach { item ->
            // Ignorar las secciones de autenticacion
            if (item.route != ROUTE_LOGIN)
                NavigationRailItem(
                    icon = {
                        Icon(
                            ImageVector.vectorResource(item.icon),
                            contentDescription = stringResource(item.contentDescription)
                        )
                    },
//                    label = { Text(text = stringResource(item.label)) },
//                    alwaysShowLabel = true,
                    selected = currentRoute == item.route,
                    onClick = { onNavigateToRoute(item.route) }
                )
        }
    }
}

@Preview
@Composable
fun RailBarPreview() {
    RailBar(currentRoute = AppDestinations.LISTS.route, onNavigateToRoute = {})
}
