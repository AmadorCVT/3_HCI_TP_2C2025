package com.example.listi.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow



import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.listi.R
import com.example.listi.ui.theme.*

import com.example.listi.ui.types.ShoppingList



@Composable
fun ShoppingListCard(
    shoppingList: ShoppingList,
    modifier: Modifier = Modifier,
    onEditClick: (ShoppingList) -> Unit,
    onShareClick:(ShoppingList)->Unit,
    onDeleteClick:(ShoppingList)->Unit

) {

    var menuExpanded by remember { mutableStateOf(false) }


    val actions = listOf(
        ActionItem(
            label = stringResource(R.string.edit),
            onClick = { onEditClick(shoppingList) }
        ),
        ActionItem(
            label = stringResource(R.string.share),
            onClick = { onShareClick(shoppingList) }
        ),
        ActionItem(
            label = stringResource(R.string.delete),
            onClick = { onDeleteClick(shoppingList) }
        )
    )

    Surface(
        modifier = modifier
            .widthIn(max = 600.dp)
            .padding(4.dp),
        shape = RoundedCornerShape(dimensionResource(R.dimen.medium_radius)),
        shadowElevation = 6.dp,
        tonalElevation = 2.dp,
        color = MaterialTheme.colorScheme.surface
    ) {

        Row(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(dimensionResource(R.dimen.medium_radius))
                )
                .padding(dimensionResource(R.dimen.medium_padding))
        ) {


            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = shoppingList.name,
                    color = MaterialTheme.colorScheme.onSurfaceVariant, //TODO: no me anda el color
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(2.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {

                    //TODO: mostrar mas de 3 letras
                    val count = shoppingList.sharedWith.size
                    val usersText = when {
                        count == 0 -> "Sin usuarios"
                        count == 1 -> shoppingList.sharedWith[0].name
                        count == 2 -> "${shoppingList.sharedWith[0].name}, ${shoppingList.sharedWith[1].name}"
                        else -> {
                            val first = shoppingList.sharedWith[0].name
                            val second = shoppingList.sharedWith[1].name
                            "$first, $second y ${count - 2} más"
                        }
                    }

                    Text(
                        text = usersText,
                        maxLines = 1,
                        style = Typography.labelMedium,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    val dateText = shoppingList.createdAt.take(10)
                        .replace("-", "/")

                    Text(
                        text = dateText,
                        maxLines = 1,
                        style = Typography.labelMedium,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }


            Spacer(Modifier.weight(1f))

            // --- Botón de opciones ---
            Box { // Necesario para posicionar el dropdown en el lugar exacto
                IconButton(onClick = { menuExpanded = true }) {
                    Icon(
                        ImageVector.vectorResource(R.drawable.more_vert_foreground),
                        contentDescription = "Options"
                    )
                }

                ReusedDropdownMenu(
                    expanded = menuExpanded,
                    onDismiss = { menuExpanded = false },
                    actions = actions
                )
            }
        }
    }
}



