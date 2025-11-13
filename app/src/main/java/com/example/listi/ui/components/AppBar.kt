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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.example.listi.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar() {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Shopping Lists",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
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
        actions = {
            IconButton(onClick = { /* handle menu */ }) {
                Icon(
                    painterResource(R.drawable.person_foreground),
                    contentDescription = "Profile"
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

@Preview
@Composable
fun AppTopBarPreview() {
    AppTopBar()
}