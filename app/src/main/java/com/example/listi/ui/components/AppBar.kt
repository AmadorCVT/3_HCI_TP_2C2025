package com.example.listi.ui.components

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.example.listi.R
import com.example.listi.ui.navigation.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(title: String) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.headlineMedium
            )
        },
        navigationIcon = {
            IconButton(onClick = { /* handle back */ }) {
                Icon(
                    painterResource(R.drawable.arrow_back_foreground),
                    contentDescription = "Back"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        scrollBehavior = scrollBehavior
    )
}

@Composable
fun routeToTitle(route: String): String {
    val ctx = LocalContext.current

    return when (route) {
        ROUTE_LISTS -> ctx.getString(R.string.lists)
        ROUTE_PRODUCTS -> ctx.getString(R.string.products)
        ROUTE_PROFILE -> ctx.getString(R.string.profile)
        ROUTE_FRIENDS -> ctx.getString(R.string.friends)
        ROUTE_LOGIN -> ctx.getString(R.string.login)
        ROUTE_REGISTER -> ctx.getString(R.string.register)
        ROUTE_VERIFY -> ctx.getString(R.string.verify)
        ROUTE_PASSWORD_CODE -> ctx.getString(R.string.route_password_code_title)
        ROUTE_PASSWORD -> ctx.getString(R.string.route_password_title)

        // Rutas que tienen argumentos dinámicos
        else -> {
            if (route.startsWith(Constants.ROUTE_LIST_DETAILS))
                ctx.getString(R.string.route_lists_title) // título especial
            else route
        }
    }
}

@Preview
@Composable
fun AppTopBarPreview() {
    AppTopBar("Shopping Lists")
}