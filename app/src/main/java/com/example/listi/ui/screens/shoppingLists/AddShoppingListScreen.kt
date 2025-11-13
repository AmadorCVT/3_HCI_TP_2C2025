package com.example.listi.ui.screens.shoppingLists

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.listi.R
import com.example.listi.ui.theme.ListiTheme

// Source: https://www.waseefakhtar.com/android/form-using-jetpack-compose-and-material-design/

@Composable
fun AddShoppingListsScreen(
    modifier: Modifier = Modifier
) {
    var shoppingListName by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(16.dp, 16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Text(
            text = stringResource(id = R.string.lists),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.displaySmall
        )

        Spacer(modifier = Modifier.padding(8.dp))

        Text(
            text = stringResource(id = R.string.active_lists),
            style = MaterialTheme.typography.bodyLarge
        )

        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = shoppingListName,
            onValueChange = { shoppingListName = it },
            placeholder = { Text(text = "e.g. Hexamine") },
        )
    }
}


@Preview(showBackground = true, showSystemUi = true, device = "spec:width=411dp,height=891dp")
@Composable
fun AddShoppingListsScreenPreview() {
    ListiTheme {
        AddShoppingListsScreen()
    }
}