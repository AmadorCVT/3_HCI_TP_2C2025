package com.example.listi.ui.screens.shoppingLists

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.listi.R
import com.example.listi.network.RetrofitInstance
import com.example.listi.repository.CategoryRepository
import com.example.listi.ui.components.GreenAddButton
import com.example.listi.ui.components.ShoppingListCard
import com.example.listi.ui.screens.products.CategoryViewModel
import com.example.listi.ui.screens.products.CategoryViewModelFactory
import com.example.listi.ui.screens.products.ProductViewModel
import com.example.listi.ui.screens.products.ProductViewModelFactory
import com.example.listi.ui.theme.ListiTheme
import com.example.listi.ui.types.CreateShoppingListRequest
import com.example.listi.ui.types.ShoppingList
import com.example.listi.ui.types.User
import java.util.Date


private val user1 = User(1, "Ama", "Doe", "ama@mail.com", null, null);
private val user2 = User(2, "Lucas", "Doe", "ama@mail.com", null, null);
private val user3 = User(3, "Bauti", "Doe", "ama@mail.com", null, null);
private val shoppingListsPreview = listOf(
    ShoppingList(1, "Lista resi",
        "Una lista",
        metadata = "",
        false,
        user1,
        arrayOf(user1, user2),
        Date().toString(),
        Date().toString(),
        Date().toString()),
    ShoppingList(2,"Lista super",
        "Una lista",
        metadata = "",
        true,
        user1,
        arrayOf(user3, user2),
        Date().toString(),
        Date().toString(),
        Date().toString()),
    ShoppingList(3, "Juntada",
        "Una lista",
        metadata = "",
        true,
        user1,
        arrayOf(user1, user3),
        Date().toString(),
        Date().toString(),
        Date().toString())
)

@Composable
fun ShoppingListsScreen(
    modifier: Modifier = Modifier,
    shoppingListViewModel: ShoppingListsViewModel = viewModel(factory = ShoppingListsViewModelFactory())
    ) {

    val shoppingLists by shoppingListViewModel.shoppingLists.collectAsState()

    // Apenas se abre quiero que se haga fetch
    LaunchedEffect(Unit) {
        shoppingListViewModel.loadShoppingLists()
    }

    // TODO: Agregar navigator al lambda
    ShoppingListsCards(modifier, shoppingLists,  {})

    val openCreateDialog = remember { mutableStateOf(false) }

    GreenAddButton(
        {
            openCreateDialog.value = true
        },
        modifier
    )

    when {
        openCreateDialog.value -> {
            CreateShoppingListDialog(
                onDismissRequest = { openCreateDialog.value = false },
                onConfirmation = {
                    openCreateDialog.value = false
                    shoppingListViewModel.createShoppingLists(CreateShoppingListRequest(
                        "tes33333dddd9",
                        "Una lista de prueba",
                        true
                    ))
                },
                dialogTitle = "Crear lista de prueba",
                dialogText = "Confirma para crear una lista!!"
            )
        }
    }

}

// NOTE: Testing!
@Composable
fun CreateShoppingListDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
) {
    AlertDialog(
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Dismiss")
            }
        }
    )
}

@Composable
fun ShoppingListsCards(
    modifier: Modifier = Modifier,
    shoppingLists: MutableList<ShoppingList>,
    onShoppingListDetails: () -> Unit,
) {

    val padding = dimensionResource(R.dimen.medium_padding)
    var selectedButton by remember { mutableStateOf("Activas") }

    val filteredLists = shoppingLists.filter {
        if (selectedButton == "Activas") {
            !it.recurring
        } else {
            it.recurring
        }
    }

    Column(modifier = modifier.fillMaxSize()) {

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = padding),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button(
                onClick = { selectedButton = "Activas" },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedButton == "Activas") Color(0xFF006400) else Color.Transparent,
                    contentColor = if (selectedButton == "Activas") Color.White else Color.Black
                )
            ) {
                Text(stringResource(R.string.active_lists))
            }
            Button(
                onClick = { selectedButton = "Guardadas" },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedButton == "Guardadas") Color(0xFF006400) else Color.Transparent,
                    contentColor = if (selectedButton == "Guardadas") Color.White else Color.Black
                )
            ) {
                Text(stringResource(R.string.saved_lists))
            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.small_padding) / 4),
            contentPadding = PaddingValues(horizontal = padding, vertical = padding),
            modifier = Modifier.clickable { onShoppingListDetails() }
        ) {
            items(items = filteredLists) { item ->
                ShoppingListCard(item, Modifier.padding(10.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShoppingListsPreview() {
    ListiTheme {
        ShoppingListsCards(
            Modifier,
            shoppingListsPreview as MutableList<ShoppingList>,
            {},
        )

        GreenAddButton(
            {},
            Modifier
        )
    }
}
