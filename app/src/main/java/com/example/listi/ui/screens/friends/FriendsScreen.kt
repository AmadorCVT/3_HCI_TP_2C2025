package com.example.listi.ui.screens.friends


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
    friendViewModel: FriendsViewModel = viewModel(factory = FriendsViewModelFactory(authViewModel)),
) {
    // Para que sea reactivo uso referencia
    val friendList by friendViewModel.friends.collectAsState()

    // Fetch friends
    LaunchedEffect(Unit) {
        friendViewModel.loadFriends()
    }

    FriendsCardList(modifier, friendList)
}

@Composable
fun FriendsCardList(
    modifier: Modifier = Modifier,
    friendList: MutableList<Friend>
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

        Spacer(modifier = Modifier.height(60.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth(),
            label = {stringResource(R.string.search_friends)},
            leadingIcon = {
                Icon(
                    ImageVector.vectorResource(R.drawable.search),
                    contentDescription = "Icono de búsqueda"
                )
            },
            singleLine = true,

            colors = TextFieldDefaults.colors(
                // Fondo del campo de texto
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant, // Verde oscuro al hacer foco
                unfocusedContainerColor = MaterialTheme.colorScheme.onPrimary, // Verde estándar
                disabledContainerColor = Color.Gray,

                // Color del texto y los iconos
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedLabelColor = MaterialTheme.colorScheme.surface,
                unfocusedLabelColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                focusedLeadingIconColor = MaterialTheme.colorScheme.surface,
                unfocusedLeadingIconColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),

                // Color del cursor
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 8.dp)
            ) {
                // NOTA: Usar LazyColumn es más eficiente para listas largas
                // que forEach, ya que solo renderiza los elementos visibles.
                // Si la lista de amigos puede crecer, considera cambiar esto.
                filteredFriends.forEach { friend ->
                    FriendCard(
                        friendName = friend.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp, horizontal = 12.dp)
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun FriendsScreenPreview() {
    ListiTheme {
        FriendsCardList(Modifier, friendListPreview as MutableList<Friend>)
    }
}

