package com.example.listi.ui.screens.shoppingLists

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.listi.R
import com.example.listi.ui.theme.ListiTheme
import com.example.listi.ui.theme.Typography

private val shoppingLists = listOf(
    ShoppingList("Lista resi", arrayOf("Ama", "Lucas")),
    ShoppingList("Lista super", arrayOf("Bauti", "Lucas", "Jaime")),
    ShoppingList("Juntada", arrayOf("Bauti", "Lucas", "Jaime", "Ama"))
)

@Composable
fun ShoppingLists(
    modifier: Modifier = Modifier
) {
    val padding = dimensionResource(R.dimen.medium_padding)

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(padding),
        contentPadding = PaddingValues(horizontal = padding),
        modifier = modifier.fillMaxSize()
    ) {
        items(items = shoppingLists) { item ->
            ShoppingListCard(item.name, item.collaborators, Modifier.padding(10.dp))
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ShoppingListsPreview() {
    ListiTheme {
        ShoppingLists()
    }
}