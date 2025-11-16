package com.example.listi.ui.screens.friends


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.listi.ui.theme.ListiTheme
import com.example.listi.ui.components.FriendCard
import com.example.listi.ui.types.Friend
import com.example.listi.ui.components.WhiteBoxWithText
import com.example.listi.R
import com.example.listi.network.RetrofitInstance
import com.example.listi.repository.CategoryRepository
import com.example.listi.ui.screens.auth.AuthViewModel
import com.example.listi.ui.screens.products.CategoryViewModel
import com.example.listi.ui.screens.products.CategoryViewModelFactory
import com.example.listi.ui.screens.shoppingLists.ShoppingListsViewModel
import com.example.listi.ui.types.User
import kotlinx.coroutines.flow.filter
import kotlin.collections.filter

private val friendListPreview = listOf(
    Friend("Lucas"),
    Friend("Ana"),
    Friend("Martín"),
    Friend("Sofía"),
    Friend("Carlos"),
    Friend("Elena"),
    Friend("Javier"),
    Friend("Valentina")
)

@Composable
fun FriendsScreen(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    shoppingListViewModel: ShoppingListsViewModel
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val connectionError = stringResource(R.string.error_connection)
    val itemError = stringResource(R.string.error_item)

    val isLoading by shoppingListViewModel.isLoading.collectAsState()
    val currentUser by authViewModel.currentUser.collectAsState()
    val refreshTrigger by shoppingListViewModel.refreshTrigger.collectAsState()
    val shoppingLists by shoppingListViewModel.shoppingLists.collectAsState()
    val shoppingListsError by shoppingListViewModel.errorMessage.collectAsState()

    LaunchedEffect(refreshTrigger) {
        shoppingListViewModel.loadShoppingLists()
    }

    val userId = currentUser?.id
    val friends: List<Friend> = remember(shoppingLists) {
        shoppingLists
            .flatMap { it.sharedWith.toList() }
            .filter { it.id != userId }
            .map { Friend(it.name) }
    }

    FriendsCardList(modifier, friends)
}

@Composable
fun FriendsCardList(
    modifier: Modifier = Modifier,
    friendList: List<Friend>
) {
    var searchQuery by remember { mutableStateOf("") }

    val filteredFriends = if (searchQuery.isBlank()) {
        friendList
    } else {
        friendList.filter { it.name.contains(searchQuery, ignoreCase = true) }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        val isLargeScreen = maxWidthDp() > 600

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth(if (isLargeScreen) 0.7f else 1f)
                .align(Alignment.CenterHorizontally),
            label = { stringResource(R.string.search_friends) },
            leadingIcon = {
                Icon(
                    ImageVector.vectorResource(R.drawable.search),
                    contentDescription = "Icono de búsqueda"
                )
            },
            singleLine = true,

            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedContainerColor = MaterialTheme.colorScheme.onPrimary,
                disabledContainerColor = Color.Gray,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedLabelColor = MaterialTheme.colorScheme.surface,
                unfocusedLabelColor = MaterialTheme.colorScheme.surface,
                focusedLeadingIconColor = MaterialTheme.colorScheme.surface,
                unfocusedLeadingIconColor = MaterialTheme.colorScheme.surface,
                cursorColor = MaterialTheme.colorScheme.surface
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        WhiteBoxWithText(
            text = "",
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {

            val screenWidth = maxWidthDp()
            val columns = calculateColumns(screenWidth)

            LazyVerticalGrid(
                columns = GridCells.Fixed(columns),
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(12.dp)
            ) {
                items(filteredFriends) { friend ->
                    FriendCard(
                        friendName = friend.name,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun maxWidthDp(): Int {
    return LocalConfiguration.current.screenWidthDp
}

fun calculateColumns(screenWidthDp: Int): Int {
    val minCardWidth = 240 // tamaño mínimo por tarjeta
    return maxOf(1, screenWidthDp / minCardWidth)
}

@Preview(showBackground = true)
@Preview(showBackground = true, showSystemUi = true, device = "spec:width=900dp,height=1400dp")
@Preview(showBackground = true, showSystemUi = true, device = "spec:width=900dp,height=1400dp, orientation=landscape")
@Composable
fun FriendsScreenPreview() {
    ListiTheme {
        FriendsCardList(Modifier, friendListPreview as MutableList<Friend>)
    }
}

