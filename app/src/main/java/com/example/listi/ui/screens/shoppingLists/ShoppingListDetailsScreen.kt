package com.example.listi.ui.screens.shoppingLists

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.listi.R
import com.example.listi.ui.components.DeleteDialog
import com.example.listi.ui.components.GreenAddButton
import com.example.listi.ui.components.ProductRow
import com.example.listi.ui.components.ShoppingListDialog
import com.example.listi.ui.components.ShoppingListItemDialog
import com.example.listi.ui.components.WhiteBoxWithText
import com.example.listi.ui.screens.friends.FriendsViewModel
import com.example.listi.ui.screens.friends.FriendsViewModelFactory
import com.example.listi.ui.screens.products.ProductViewModel
import com.example.listi.ui.theme.ListiTheme
import com.example.listi.ui.types.Category
import com.example.listi.ui.types.Product
import com.example.listi.ui.types.ShoppingList
import com.example.listi.ui.types.ShoppingListItem
import com.example.listi.ui.types.ShoppingListItemRequest
import kotlinx.coroutines.launch
import java.util.Date

// Source: https://www.waseefakhtar.com/android/form-using-jetpack-compose-and-material-design/

private val item1 = ShoppingListItem(
    1,
    "KG",
    2,
    true,
    "asd22",
    Date().toString(),
    Date().toString(),
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
    Date().toString(),
    Date().toString(),
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
    productViewModel: ProductViewModel,
    shoppingListViewModel: ShoppingListsViewModel,
    shoppingListItemsViewModel: ShoppingListItemsViewModel = viewModel(factory = ShoppingListItemsViewModelFactory()),
    listId: Int
) {

    // Para mostrar errores
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val errorColor = MaterialTheme.colorScheme.error
    val connectionError = stringResource(R.string.error_connection)

    val openCreateDialog = remember { mutableStateOf(false) }
    val openDeleteDialog = remember { mutableStateOf(false) }
    val itemToDelete = remember { mutableStateOf<ShoppingListItem?>(null) }

    // Buscar la lista que nos piden
    val shoppingListItems by shoppingListItemsViewModel.items.collectAsState()
    val shoppingLists by shoppingListViewModel.shoppingLists.collectAsState()
    val products by productViewModel.products.collectAsState()

    val listsRefreshTrigger by shoppingListItemsViewModel.refreshTrigger.collectAsState()
    val itemsRefreshTrigger by shoppingListViewModel.refreshTrigger.collectAsState()
    val productRefreshTrigger  by productViewModel.refreshTrigger.collectAsState()

    val shoppingListItemsError by shoppingListItemsViewModel.errorMessage.collectAsState()

    // Apenas se abre o cambie el refreshTrigger quiero que se haga fetch
    LaunchedEffect(itemsRefreshTrigger) {
        shoppingListItemsViewModel.loadShoppingListItems(listId)
    }

    LaunchedEffect(listsRefreshTrigger) {
        shoppingListViewModel.loadShoppingLists()
    }

    LaunchedEffect(productRefreshTrigger) {
        productViewModel.loadProducts()
    }

    // Si sale un mensaje de error, mostrarlo
    LaunchedEffect(shoppingListItemsError) {
        shoppingListItemsError?.let {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = shoppingListItemsError.toString()
                )
            }
        }
        shoppingListItemsViewModel.clearError()
    }

    when {
        openCreateDialog.value -> {
            ShoppingListItemDialog(
                onDismissRequest = { openCreateDialog.value = false },
                onConfirmation = { shoppingListItemRequest ->
                    shoppingListItemsViewModel.createShoppingListsItem(listId, shoppingListItemRequest)
                    openCreateDialog.value = false
                },
                products = products
            )
        }
        openDeleteDialog.value -> {
            DeleteDialog(
                name = itemToDelete.value?.product?.name ?: "-",
                onDismiss = {
                    openDeleteDialog.value = false},
                onConfirm = {

                    shoppingListItemsViewModel.deleteShoppingListItem(listId,
                        itemToDelete.value?.id ?: -1
                    )
                    openDeleteDialog.value = false
                }
            )
        }
    }

    // Scaffold para el snackbar
    Scaffold(
        snackbarHost = { SnackbarHost( snackbarHostState) }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top
            ) {
                ShoppingListItemsHeader(modifier, shoppingLists.first { it.id == listId })
                AddedShoppingListItem(
                    modifier,
                    shoppingListItems,
                    onStatusToggle = { itemId ->
                        shoppingListItemsViewModel.toggleStatusShoppingListItem(listId, itemId)
                    },
                    onDelete = { item ->
                        itemToDelete.value = item
                        openDeleteDialog.value = true
                    }
                )
            }

            GreenAddButton(
                {
                    openCreateDialog.value = true
                }
            )
        }
    }
}

@Composable
fun ShoppingListItemsHeader(
    modifier: Modifier = Modifier,
    list: ShoppingList
) {
    Column {
        Text(
            text = list.name,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(16.dp),
        )

        Text(
            text = list.description,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Composable
fun AddedShoppingListItem(
    modifier: Modifier = Modifier,
    items: List<ShoppingListItem>,
    onStatusToggle: (Int) -> (Unit),
    onDelete: (ShoppingListItem) -> (Unit)
) {
    // === WhiteBox con los productos ===
    WhiteBoxWithText(
        text = "",
        modifier = Modifier
            .fillMaxWidth()
    ) {
        LazyColumn {
            item { HeaderRow() }
            items(items) { item ->
                ProductRow(
                    item = item,
                    onCheckedChange = {
                        onStatusToggle(item.id)
                    },
                    onDelete = { onDelete(item) })
            }
        }
    }
}

@Composable
fun HeaderRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.products),
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.surfaceVariant
        )

        Text(
            text = stringResource(R.string.quantity),
            modifier = Modifier.weight(0.65f),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.surfaceVariant
        )

        Text(
            text = stringResource(R.string.unit),
            modifier = Modifier.weight(0.55f),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.surfaceVariant
        )

        Spacer(modifier = Modifier.weight(0.3f))
    }

    Divider(
        color = Color(0xFFBDBDBD),
        thickness = 1.dp,
        modifier = Modifier.padding(top = 2.dp)
    )
}

@Preview(showBackground = true, showSystemUi = true, device = "spec:width=411dp,height=891dp")
@Composable
fun AddShoppingListsScreenPreview() {
    ListiTheme {
        AddedShoppingListItem(Modifier, ShoppingListItemsPreview, {}, {})
    }
}