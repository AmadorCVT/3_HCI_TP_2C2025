package com.example.listi.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.listi.R
import com.example.listi.ui.theme.DarkGrey
import com.example.listi.ui.theme.LightGreen
import com.example.listi.ui.theme.ListiTheme
import com.example.listi.ui.theme.Typography

@Composable
fun ShoppingListCard(
    name: String,
    collaborators: Array<String>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(
            border = BorderStroke(4.dp, DarkGrey), // Thicker border
            shape = RoundedCornerShape(dimensionResource(R.dimen.medium_radius))      // Rounded corners
        )
            .background(
                color = LightGreen,
                shape = RoundedCornerShape(dimensionResource(R.dimen.medium_radius))
            )
            .padding(dimensionResource(R.dimen.medium_padding))
    ) {

        // List name and collaborators names
        ShoppingListData(name, collaborators, modifier)

        Spacer(Modifier.weight(1f))

        // Options button
        OptionsButton({})
    }
}

@Composable
fun ShoppingListData(
    name: String,
    collaborators: Array<String>,
    modifier: Modifier = Modifier
) {

    Column (
        modifier = modifier,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            name,
            maxLines = 1,
            style = Typography.bodyMedium,
            overflow = TextOverflow.Ellipsis
        )

        LazyRow(
            modifier = modifier,
        ) {
            items(items = collaborators) { item ->
                Text(
                    "$item, ",
                    maxLines = 1,
                    style = Typography.labelMedium,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun OptionsButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(onClick = { onClick() }) {
        Icon(
            painterResource(R.drawable.more_vert_foreground),
            contentDescription = "Button"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ShoppingListCardPreview() {
    ListiTheme {
        ShoppingListCard(
            "Lista Resi",
            arrayOf("humano 1", "humano 2", "humano 3", "animal 1"),
            modifier = Modifier.padding(10.dp)
        )

    }
}