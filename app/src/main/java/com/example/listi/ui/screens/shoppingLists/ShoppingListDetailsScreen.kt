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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
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
import com.example.listi.network.RetrofitInstance
import com.example.listi.repository.CategoryRepository
import com.example.listi.ui.components.DeleteDialog
import com.example.listi.ui.components.GreenAddButton
import com.example.listi.ui.components.ProductRow
import com.example.listi.ui.components.ShoppingListDialog
import com.example.listi.ui.components.ShoppingListItemDialog
import com.example.listi.ui.components.WhiteBoxWithText
import com.example.listi.ui.screens.friends.FriendsViewModel
import com.example.listi.ui.screens.friends.FriendsViewModelFactory
import com.example.listi.ui.screens.products.CategoryViewModel
import com.example.listi.ui.screens.products.CategoryViewModelFactory
import com.example.listi.ui.screens.products.ProductViewModel
import com.example.listi.ui.theme.LightGreen
import com.example.listi.ui.theme.ListiTheme
import com.example.listi.ui.theme.White
import com.example.listi.ui.types.Category
import com.example.listi.ui.types.Product
import com.example.listi.ui.types.ShoppingList
import com.example.listi.ui.types.ShoppingListItem
import com.example.listi.ui.types.ShoppingListItemRequest
import kotlinx.coroutines.launch
import java.util.Date



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
    categoryViewModel: CategoryViewModel,
    shoppingListViewModel: ShoppingListsViewModel,
    shoppingListItemsViewModel: ShoppingListItemsViewModel = viewModel(factory = ShoppingListItemsViewModelFactory()),
    listId: Int
) {

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val categories by categoryViewModel.categories.collectAsState()
    val categoryError by categoryViewModel.errorMessage.collectAsState()
    val categoryRefreshTrigger  by categoryViewModel.refreshTrigger.collectAsState()

    val openCreateDialog = remember { mutableStateOf(false) }
    val openDeleteDialog = remember { mutableStateOf(false) }
    val itemToDelete = remember { mutableStateOf<ShoppingListItem?>(null) }

    val shoppingListItems by shoppingListItemsViewModel.items.collectAsState()
    val shoppingLists by shoppingListViewModel.shoppingLists.collectAsState()
    val products by productViewModel.products.collectAsState()

    val isLoading by shoppingListItemsViewModel.isLoading.collectAsState()

    val listsRefreshTrigger by shoppingListItemsViewModel.refreshTrigger.collectAsState()
    val itemsRefreshTrigger by shoppingListViewModel.refreshTrigger.collectAsState()
    val productRefreshTrigger by productViewModel.refreshTrigger.collectAsState()

    val shoppingListError by shoppingListViewModel.errorMessage.collectAsState()
    val shoppingListItemsError by shoppingListItemsViewModel.errorMessage.collectAsState()

    val connectionError = stringResource(R.string.error_connection)
    val addError = stringResource(R.string.error_item)
    val purchaseError = stringResource(R.string.error_purchase)
    val purchasedMessage = stringResource(R.string.purchased)
    val resetMessage = stringResource(R.string.reseted)
    val itemError = stringResource(R.string.error_item)

    LaunchedEffect(itemsRefreshTrigger) {
        shoppingListItemsViewModel.loadShoppingListItems(listId)
    }

    LaunchedEffect(listsRefreshTrigger) {
        shoppingListViewModel.loadShoppingLists()
    }

    LaunchedEffect(productRefreshTrigger) {
        productViewModel.loadProducts()
    }

    LaunchedEffect(categoryRefreshTrigger) {
        categoryViewModel.loadCategories()
    }

    LaunchedEffect(categoryError) {
        categoryError?.let {
            var message = connectionError

            when(categoryError) {
                "409" ->  message = itemError
                else -> connectionError
            }

            scope.launch {
                snackbarHostState.showSnackbar(
                    message = message
                )
            }
        }
        categoryViewModel.clearError()
    }

    LaunchedEffect(shoppingListItemsError) {
        shoppingListItemsError?.let {
            val msg = if (it == "409") addError else connectionError
            scope.launch { snackbarHostState.showSnackbar(shoppingListItemsError.toString()) }
        }
        shoppingListItemsViewModel.clearError()
    }

    LaunchedEffect(shoppingListError) {
        shoppingListError?.let {
            val msg = when (it) {
                "400" -> purchaseError
                "409" -> addError
                else -> connectionError
            }
            scope.launch { snackbarHostState.showSnackbar(shoppingListError.toString()) }
        }
        shoppingListViewModel.clearError()
    }


    when {
        openCreateDialog.value -> ShoppingListItemDialog(
            onDismissRequest = { openCreateDialog.value = false },
            onConfirmation = {
                shoppingListItemsViewModel.createShoppingListsItem(listId, it)
                openCreateDialog.value = false
            },
            addProduct = {productRequest ->
                productViewModel.createProduct(productRequest)
                         },
            products = products,
            categories = categories
        )

        openDeleteDialog.value -> DeleteDialog(
            name = itemToDelete.value?.product?.name ?: "-",
            onDismiss = { openDeleteDialog.value = false },
            onConfirm = {
                shoppingListItemsViewModel.deleteShoppingListItem(
                    listId,
                    itemToDelete.value?.id ?: -1
                )
                openDeleteDialog.value = false
            }
        )
    }


    if (isLoading) {
        Box(Modifier.fillMaxSize(), Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {


                ShoppingListItemsHeader(
                    list = shoppingLists.first { it.id == listId }
                )

                Spacer(Modifier.height(12.dp))


                AddedShoppingListItem(
                    items = shoppingListItems,
                    onStatusToggle = { id, purchased ->
                        shoppingListItemsViewModel.toggleStatusShoppingListItem(
                            listId,
                            id,
                            purchased
                        )
                    },
                    onDelete = {
                        itemToDelete.value = it
                        openDeleteDialog.value = true
                    },
                    modifier = Modifier.weight(1f)
                )

                Spacer(Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    Button(
                        onClick = {
                            shoppingListViewModel.purchaseShoppingLists(listId)
                            scope.launch {
                                snackbarHostState.showSnackbar(purchasedMessage)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = LightGreen),
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.purchase),
                            color = White,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Button(
                        onClick = {
                            shoppingListViewModel.resetShoppingLists(listId)
                            scope.launch {
                                snackbarHostState.showSnackbar(resetMessage)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = LightGreen),
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.reset),
                            color = White,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }


            GreenAddButton(
                onClick = { openCreateDialog.value = true },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
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
    onStatusToggle: (Int, Boolean) -> (Unit),
    onDelete: (ShoppingListItem) -> (Unit)
) {

    WhiteBoxWithText(
        text = "",
        modifier = Modifier
            .fillMaxWidth()
    ) {
        LazyColumn {
            item { HeaderRow() }
            items(items) { item ->
                if (item.product != null)
                    ProductRow(
                        item = item,
                        onCheckedChange = {
                            onStatusToggle(item.id, !item.purchased)
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
fun ShoppingListDetailsPreview() {
    ListiTheme {
        Box(modifier = Modifier.fillMaxSize()) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {

                // Simula encabezado
                Text(
                    text = "Shopping list example",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                // 🟩 EXACTO COMO EN TU PANTALLA REAL
                AddedShoppingListItem(
                    items = ShoppingListItemsPreview,
                    onStatusToggle = { _, _ -> },
                    onDelete = {},
                    modifier = Modifier.weight(1f)
                )

                Spacer(Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    Button(
                        onClick = { },
                        colors = ButtonDefaults.buttonColors(containerColor = LightGreen),
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.purchase),
                            color = White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Button(
                        onClick = { },
                        colors = ButtonDefaults.buttonColors(containerColor = LightGreen),
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.reset),
                            color = White,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            GreenAddButton(
                onClick = {},
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            )
        }
    }
}