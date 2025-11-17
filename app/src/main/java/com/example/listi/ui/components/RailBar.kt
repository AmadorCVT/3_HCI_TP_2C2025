package com.example.listi.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.listi.R
import com.example.listi.ui.navigation.AppDestinations
import com.example.listi.ui.navigation.ROUTE_LOGIN

//TODO: cuando seleccionas un lugar es lila, como el predeterminado de MD3
@Composable
fun RailBar(
    currentRoute: String,
    onNavigateToRoute: (String) -> Unit
) {
    NavigationRail(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        header = {
            Column(
                modifier = Modifier.padding(vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val logoSize = 65.dp
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier.size(logoSize)
                )
            }
        }
    ) {
        AppDestinations.entries.forEach { item ->
            if (item.route != ROUTE_LOGIN)
                NavigationRailItem(
                    icon = {
                        Icon(
                            ImageVector.vectorResource(item.icon),
                            contentDescription = stringResource(item.contentDescription)
                        )
                    },
                    selected = currentRoute == item.route,
                    onClick = { onNavigateToRoute(item.route) },
                    colors = NavigationRailItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.surface,
                        selectedTextColor = MaterialTheme.colorScheme.surface,
                        indicatorColor = MaterialTheme.colorScheme.surfaceVariant,
                        unselectedIconColor = MaterialTheme.colorScheme.secondary,
                        unselectedTextColor = MaterialTheme.colorScheme.secondary
                    )
                )
        }
    }
}

@Preview
@Composable
fun RailBarPreview() {
    RailBar(currentRoute = AppDestinations.LISTS.route, onNavigateToRoute = {})
}
