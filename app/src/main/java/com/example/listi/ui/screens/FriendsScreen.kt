package com.example.listi.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.listi.ui.theme.ListiTheme
import com.example.listi.ui.components.FriendCard
import com.example.listi.ui.types.Friend

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
    // Estado para guardar el texto de búsqueda
    var searchQuery by remember { mutableStateOf("") }

    // Filtra la lista de amigos basándose en la búsqueda (ignora mayúsculas/minúsculas)
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

        // Contenedor con la lista vertical de amigos
        Surface(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f), // Un color suave de fondo
            tonalElevation = 1.dp
        ) {
            LazyColumn(
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                items(items = filteredFriends) { friend ->
                    FriendCard(
                        friendName = friend.name,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
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


