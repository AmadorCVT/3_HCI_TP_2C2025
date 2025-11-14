package com.example.listi.ui.screens.shoppingLists

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.listi.R
import com.example.listi.ui.components.WhiteBoxWithText
import com.example.listi.ui.theme.ListiTheme
import com.example.listi.ui.types.Category
import com.example.listi.ui.types.Product
import com.example.listi.ui.types.ShoppingListItem
import java.util.Date

// Source: https://www.waseefakhtar.com/android/form-using-jetpack-compose-and-material-design/

private val item1 = ShoppingListItem(
    1,
    "KG",
    2,
    true,
    "asd22",
    Date(),
    Date(),
    Product(
        2,
        "Papas",
        "as",
        "ads",
        Category(1, "verdura", "gfb", "asfd"))
)
private val item2 = ShoppingListItem(
    1,
    "KG",
    2,
    true,
    "asd22",
    Date(),
    Date(),
    Product(
        2,
        "Papas",
        "as",
        "ads",
        Category(1, "verdura", "gfb", "asfd"))
)

private val ShoppingListItemsPreview = listOf<ShoppingListItem>(item1, item2)

@Composable
fun ShoppingListDetailsScreen(
    modifier: Modifier = Modifier,
    listId: Int
) {
    var shoppingListName by remember { mutableStateOf("") }
    var recurring by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp, 16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Spacer(modifier = Modifier.padding(8.dp))

        // Seguir reglas de jerarquia con funciones que modifican el estado desde aca arriba
        ShoppingListHeader(
            shoppingListName = shoppingListName,
            onShoppingListNameChange = { shoppingListName = it },
            recurring = recurring,
            onRecurringChange = { recurring = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        AddedShoppingListItem(modifier, ShoppingListItemsPreview)
    }
}

// TODO: Check reactiveness
@Composable
fun ShoppingListHeader(
    modifier: Modifier = Modifier,
    shoppingListName: String,
    onShoppingListNameChange: (String) -> Unit,
    recurring: Boolean,
    onRecurringChange: (Boolean) -> Unit
) {
    Text(
        text = stringResource(id = R.string.name),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurface
    )

    OutlinedTextField(
        value = shoppingListName,
        onValueChange = onShoppingListNameChange,
        label = { Text(stringResource(R.string.name)) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.padding(8.dp))

    Row (
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        modifier = Modifier.clickable { onRecurringChange(!recurring) }.fillMaxWidth().padding(16.dp)
    ) {
        Checkbox(checked = recurring, onCheckedChange = { onRecurringChange(it) })
        Text(stringResource(R.string.recurring))
    }

}

@Composable
fun AddedShoppingListItem(
    modifier: Modifier = Modifier,
    shoppingListItems: List<ShoppingListItem>
) {
    WhiteBoxWithText(
        text = "",
        modifier = modifier
            .fillMaxWidth()
            .fillMaxSize()
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(vertical = 8.dp)
        ) {
            // NOTA: Usar LazyColumn es más eficiente para listas largas
            // que forEach, ya que solo renderiza los elementos visibles.
            // Si la lista de amigos puede crecer, considera cambiar esto.
            shoppingListItems.forEach { item ->
                Text(item.product.name)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, device = "spec:width=411dp,height=891dp")
@Composable
fun AddShoppingListsScreenPreview() {
    ListiTheme {
        var shoppingListName by remember { mutableStateOf("") }
        var recurring by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp, 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Spacer(modifier = Modifier.padding(8.dp))

            // Seguir reglas de jerarquia con funciones que modifican el estado desde aca arriba
            ShoppingListHeader(
                shoppingListName = shoppingListName,
                onShoppingListNameChange = { shoppingListName = it },
                recurring = recurring,
                onRecurringChange = { recurring = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            AddedShoppingListItem(Modifier, ShoppingListItemsPreview)
        }
    }
}