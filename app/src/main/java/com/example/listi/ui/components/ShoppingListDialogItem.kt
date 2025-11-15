package com.example.listi.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.listi.R
import com.example.listi.ui.theme.ListiTheme
import com.example.listi.ui.types.Product
import com.example.listi.ui.types.ShoppingList
import com.example.listi.ui.types.ShoppingListItem
import com.example.listi.ui.types.ShoppingListRequest

@Composable
fun ShoppingListItemDialog(
    shoppingListItem: ShoppingListItem? = null,
    title: String,
    onDismissRequest: () -> Unit,
    onConfirmation: (ShoppingListRequest) -> Unit,
) {

   // var product by remember { mutableStateOf(Product(0, )) }
    var unit by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf(0) }

    if (shoppingListItem != null) {
      //  product = shoppingListItem.product
        unit = shoppingListItem.unit
        quantity = shoppingListItem.quantity
    }
//
//    Dialog(
//        onDismissRequest = {
//            onDismissRequest()
//        },
//
//    ) {
//        Card(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(450.dp)
//                .padding(16.dp),
//            shape = RoundedCornerShape(16.dp),
//        ) {
//            Column(
//                modifier = Modifier
//                    .fillMaxSize(),
//                verticalArrangement = Arrangement.Center,
//                horizontalAlignment = Alignment.CenterHorizontally,
//            ) {
//                Text(
//                    text = title,
//                    style = MaterialTheme.typography.titleMedium,
//                    modifier = Modifier.padding(16.dp),
//                )
//                OutlinedTextField(
//                    value = name,
//                    onValueChange = { name = it },
//                    label = { Text(stringResource(R.string.name)) },
//                    singleLine = true,
//                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
//                    modifier = Modifier.fillMaxWidth()
//                        .padding(dimensionResource(R.dimen.medium_padding))
//                )
//                OutlinedTextField(
//                    value = description,
//                    onValueChange = { description = it },
//                    label = { Text(stringResource(R.string.generic_description)) },
//                    singleLine = true,
//                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
//                    modifier = Modifier.fillMaxWidth()
//                        .padding(dimensionResource(R.dimen.medium_padding))
//                )
//
//                Row (
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.spacedBy(24.dp),
//                    modifier = Modifier.clickable { recurring = !recurring }.fillMaxWidth().padding(16.dp)
//                ) {
//                    Checkbox(checked = recurring, onCheckedChange = { recurring = !recurring })
//                    Text(stringResource(R.string.recurring))
//                }
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceAround,
//                ) {
//                    OutlinedButton(
//                        onClick = { onDismissRequest()}
//                    ) {
//                        Text(text = stringResource(R.string.logout_confirm_cancel),
//                            color = MaterialTheme.colorScheme.surfaceVariant)
//                    }
//                    Button(
//                        onClick = { onConfirmation(ShoppingListRequest(
//                            name,
//                            description,
//                            recurring
//                        )) },
//                        colors = ButtonDefaults.buttonColors(
//                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
//                            contentColor = MaterialTheme.colorScheme.onError
//                        )
//                    ) {
//                        Text(text = stringResource(R.string.save))
//                    }
//                }
//            }
//        }
//
//    }
}

//@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
//@Composable
//fun ShoppingListDialogPreview() {
//    ListiTheme() {
//        ShoppingListDialog(
//            null,
//            "Create new list",
//            {},
//            {},
//        )
//    }
//}