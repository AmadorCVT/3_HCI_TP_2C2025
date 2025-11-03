package com.example.listi.ui.screens


import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.listi.ui.theme.ListiTheme
import com.example.listi.ui.components.FriendCard
import com.example.listi.ui.types.Friend
import com.example.listi.ui.components.WhiteBoxWithText

private val friendList = listOf(
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
fun FriendsScreen(modifier: Modifier = Modifier) {
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
        Spacer(modifier = Modifier.height(16.dp))

        // Campo de búsqueda
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Buscar amigos") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Icono de búsqueda"
                )
            },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 🔲 Uso directo del componente WhiteBoxWithText
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
        FriendsScreen()
    }
}
