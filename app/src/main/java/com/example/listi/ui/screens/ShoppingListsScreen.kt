package com.example.listi.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.listi.R
import com.example.listi.ui.components.AppTopBar
import com.example.listi.ui.components.ShoppingListCard
import com.example.listi.ui.theme.ListiTheme
import com.example.listi.ui.types.ShoppingList
import com.example.listi.ui.types.User
import java.util.Date


private val user1 = User(1, "Ama", "Doe", "ama@mail.com", Date(), Date());
private val user2 = User(2, "Lucas", "Doe", "ama@mail.com", Date(), Date());
private val user3 = User(3, "Bauti", "Doe", "ama@mail.com", Date(), Date());
private val shoppingLists = listOf(
    ShoppingList(1, "Lista resi",
        "Una lista",
        false,
        user1,
        arrayOf(user1, user2),
        Date(),
        Date(),
        Date()),
    ShoppingList(2,"Lista super",
        "Una lista",
        false,
        user1,
        arrayOf(user3, user2),
        Date(),
        Date(),
        Date()),
    ShoppingList(3, "Juntada",
        "Una lista",
        false,
        user1,
        arrayOf(user1, user3),
        Date(),
        Date(),
        Date())
)

@Composable
fun ShoppingListsScreen(
    modifier: Modifier = Modifier
) {
    val padding = dimensionResource(R.dimen.medium_padding)

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.small_padding)/4),
        contentPadding = PaddingValues(horizontal = padding, vertical = padding*4),
        modifier = modifier.fillMaxSize()
    ) {
        items(items = shoppingLists) { item ->
            ShoppingListCard(item.name, item.sharedWith, Modifier.padding(10.dp))
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ShoppingListsPreview() {
    ListiTheme {
        ShoppingListsScreen()
    }
}