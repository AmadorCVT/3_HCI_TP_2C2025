package com.example.listi.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.listi.ui.navigation.AppDestinations
import com.example.listi.ui.navigation.ROUTE_LISTS
import com.example.listi.ui.navigation.ROUTE_LOGIN
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerBar(
    currentRoute: String,
    onNavigateToRoute: (String) -> Unit
) {

    ModalDrawerSheet(
        drawerContainerColor = MaterialTheme.colorScheme.primaryContainer,
        drawerContentColor = MaterialTheme.colorScheme.onPrimaryContainer

    ) {

        Column(
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "User Profile",
                    modifier = Modifier.size(40.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Username",
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Spacer(Modifier.height(12.dp))
            AppDestinations.entries.forEach { item ->
                if (item.route != ROUTE_LOGIN) {
                    NavigationDrawerItem(
                        icon = {
                            Icon(
                                ImageVector.vectorResource(item.icon),
                                contentDescription = stringResource(item.contentDescription)
                            )
                        },
                        label = {
                            Text(text = stringResource(item.label))
                        },
                        selected = currentRoute == item.route,
                        onClick = { onNavigateToRoute(item.route) },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        }
    }
}


@Preview
@Composable
fun DrawerBarPreview() {
    DrawerBar(currentRoute = ROUTE_LISTS, onNavigateToRoute = {})
}
