package com.example.listi.ui.screens.shoppingLists

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.listi.R
import com.example.listi.ui.theme.ListiTheme

// Source: https://www.waseefakhtar.com/android/form-using-jetpack-compose-and-material-design/

@Composable
fun AddShoppingListsScreen(
    modifier: Modifier = Modifier
) {
    var shoppingListName by remember { mutableStateOf("") }
    var recurring by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(16.dp, 16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Spacer(modifier = Modifier.padding(8.dp))

        Text(
            text = stringResource(id = R.string.name),
            style = MaterialTheme.typography.bodyMedium
        )

        OutlinedTextField(
            value = shoppingListName,
            onValueChange = { shoppingListName = it },
            label = { Text(stringResource(R.string.name)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.padding(16.dp))

        Row {
            Checkbox(checked = recurring, onCheckedChange = null)
            Text(stringResource(R.string.recurring))
        }
    }
}


@Preview(showBackground = true, showSystemUi = true, device = "spec:width=411dp,height=891dp")
@Composable
fun AddShoppingListsScreenPreview() {
    ListiTheme {
        AddShoppingListsScreen()
    }
}