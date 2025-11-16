package com.example.listi.ui.screens.shoppingLists

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.example.listi.ui.components.ShareListDialog
import com.example.listi.ui.components.ShoppingListCard
import com.example.listi.ui.components.ShoppingListDialog
import com.example.listi.ui.screens.products.CategoryViewModel
import com.example.listi.ui.screens.products.CategoryViewModelFactory
import com.example.listi.ui.screens.products.ProductViewModel
import com.example.listi.ui.screens.products.ProductViewModelFactory
import com.example.listi.ui.theme.ListiTheme
import com.example.listi.ui.types.ShoppingListRequest
import com.example.listi.ui.types.ShoppingList
import com.example.listi.ui.types.User
import kotlinx.coroutines.launch
import java.util.Date
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalConfiguration
import com.example.listi.ui.components.DeleteDialog


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
        false,
        user1,
        arrayOf(user1, user3),
        Date().toString(),
        Date().toString(),
        Date().toString())
)

@Composable
fun ShoppingListsScreen(
    modifier: Modifier = Modifier,
    shoppingListViewModel: ShoppingListsViewModel,
    onNavigateToDetails: (Int) -> Unit
    ) {


    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val connectionError = stringResource(R.string.error_connection)
    val itemError = stringResource(R.string.error_item)

    val isLoading by shoppingListViewModel.isLoading.collectAsState()
    val refreshTrigger by shoppingListViewModel.refreshTrigger.collectAsState()
    val shoppingLists by shoppingListViewModel.shoppingLists.collectAsState()
    val shoppingListsError by shoppingListViewModel.errorMessage.collectAsState()


    LaunchedEffect(refreshTrigger) {
        shoppingListViewModel.loadShoppingLists()
    }



    LaunchedEffect(shoppingListsError) {
        shoppingListsError?.let {
            var message = connectionError

            when(shoppingListsError) {
                "409" ->  message = itemError
                else -> connectionError
            }

            scope.launch {
                snackbarHostState.showSnackbar(
                    message = message
                )
            }
        }
        shoppingListViewModel.clearError()
    }
    val openCreateDialog = rememberSaveable { mutableStateOf(false) }

    when {
        openCreateDialog.value -> {
            ShoppingListDialog(
                title = stringResource(R.string.create_lists),
                onDismissRequest = { openCreateDialog.value = false },
                onConfirmation = { list ->
                    shoppingListViewModel.createShoppingLists(list)
                    openCreateDialog.value = false
                }
            )
        }
    }

    when {
        isLoading -> Box(Modifier.fillMaxSize(), Alignment.Center) { CircularProgressIndicator() }

        else -> {
            Scaffold(
                snackbarHost = { SnackbarHost(snackbarHostState) }
            ) { paddingValues ->
                Box(modifier = Modifier.padding(paddingValues)) {
                    ShoppingListsCards(
                        modifier = modifier,
                        shoppingLists = shoppingLists,
                        onShoppingListDetails = onNavigateToDetails,
                        shoppingListViewModel = shoppingListViewModel,
                        onShareList = { list, email ->
                            shoppingListViewModel.shareShoppingList(list.id, email)
                        }
                    )
                    GreenAddButton(
                        { openCreateDialog.value = true },
                        modifier
                    )
                }
            }
        }
    }
}

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun ShoppingListsCards(
    modifier: Modifier = Modifier,
    shoppingLists: List<ShoppingList>,
    onShoppingListDetails: (Int) -> Unit,
    shoppingListViewModel: ShoppingListsViewModel,
    onShareList: (ShoppingList, String) -> Unit = { _, _ -> }
) {

    val padding = dimensionResource(R.dimen.medium_padding)
    var selectedButton by remember { mutableStateOf("all") }

    val filteredLists = shoppingLists.filter {
        when (selectedButton) {
            "all" -> true
            "history" -> it.lastPurchasedAt != null
            else -> it.recurring
        }
    }

    var listToShare by remember { mutableStateOf<ShoppingList?>(null) }
    var listToEdit by remember { mutableStateOf<ShoppingList?>(null) }
    var listToDelete by remember { mutableStateOf<ShoppingList?>(null) }

    Column(modifier = modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = padding),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button(
                onClick = { selectedButton = "all" },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedButton == "all") MaterialTheme.colorScheme.surfaceVariant  else Color.Transparent,
                    contentColor = if (selectedButton == "all") Color.White else Color.Black
                )
            ) {
                Text(stringResource(R.string.all))
            }

            Button(
                onClick = { selectedButton = "recurring" },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedButton == "recurring") MaterialTheme.colorScheme.surfaceVariant  else Color.Transparent,
                    contentColor = if (selectedButton == "recurring") Color.White else Color.Black
                )
            ) {
                Text(stringResource(R.string.recurring))
            }

            Button(
                onClick = { selectedButton = "history" },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedButton == "history") MaterialTheme.colorScheme.surfaceVariant else Color.Transparent,
                    contentColor = if (selectedButton == "history") Color.White else Color.Black
                )
            ) {
                Text(stringResource(R.string.history))
            }
        }

        val screenWidth = LocalConfiguration.current.screenWidthDp
        val maxCardWidth = 350
        val columns = (screenWidth / maxCardWidth).coerceAtLeast(1)

        LazyVerticalGrid(
            columns = GridCells.Fixed(columns),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = padding, vertical = padding)
        ) {
            items(
                items = filteredLists,
                key = { it.id }
            ) { item ->
                ShoppingListCard(
                    shoppingList = item,
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .clickable { onShoppingListDetails(item.id) },
                    onEditClick = { listToEdit = item },
                    onShareClick = { listToShare = item },
                    onDeleteClick = { listToDelete = item }
                )
            }
        }
    }


    if (listToShare != null) {
        ShareListDialog(
            listName = listToShare!!.name,
            onDismiss = { listToShare = null },
            onSend = { email ->
                onShareList(listToShare!!, email)
                listToShare = null
            }
        )
    }


    if (listToEdit != null) {
        ShoppingListDialog(
            title = stringResource(R.string.edit_list),
            shoppingList = listToEdit!!,
            onDismissRequest = { listToEdit = null },
            onConfirmation = { updatedRequest ->
                val updatedList = listToEdit!!.copy(
                    name = updatedRequest.name,
                    description = updatedRequest.description,
                    recurring = updatedRequest.recurring
                )


                shoppingListViewModel.updateShoppingLists(updatedList)

                listToEdit = null
            }
        )
    }
    if (listToDelete != null) {
        DeleteDialog(
            name = listToDelete!!.name,
            onDismiss = { listToDelete = null },
            onConfirm = {
                shoppingListViewModel.deleteShoppingLists(listToDelete!!.id)
                listToDelete = null
            }
        )
    }
}