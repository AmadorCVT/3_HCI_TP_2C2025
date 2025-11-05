package com.example.listi.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.listi.R
import com.example.listi.ui.theme.*

@Composable
fun ShoppingListCard(
    name: String,
    collaborators: Array<String>,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp), // margen exterior para que se vea la sombra
        shape = RoundedCornerShape(dimensionResource(R.dimen.medium_radius)),
        shadowElevation = 6.dp, // 🔥 sombra real
        tonalElevation = 2.dp, // opcional: da un pequeño relieve
        color = White // color base del fondo
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(dimensionResource(R.dimen.medium_radius))
                )
                .padding(dimensionResource(R.dimen.medium_padding))
        ) {
            // List name and collaborators names
            ShoppingListData(name, collaborators)

            Spacer(Modifier.weight(1f))

            // Options button
            OptionsButton({})
        }
    }
}

@Composable
fun ShoppingListData(
    name: String,
    collaborators: Array<String>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            name,
            maxLines = 1,
            style = Typography.bodyMedium,
            overflow = TextOverflow.Ellipsis
        )

        LazyRow {
            items(items = collaborators) { item ->
                Text(
                    "$item, ",
                    maxLines = 1,
                    style = Typography.labelMedium,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun OptionsButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(onClick = onClick) {
        Icon(
            painterResource(R.drawable.more_vert_foreground),
            contentDescription = "Button"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ShoppingListCardPreview() {
    ListiTheme {
        ShoppingListCard(
            "Lista Resi",
            arrayOf("humano 1", "humano 2", "humano 3", "animal 1"),
            modifier = Modifier.padding(10.dp)
        )
    }
}
