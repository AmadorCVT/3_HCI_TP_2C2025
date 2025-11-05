package com.example.listi.ui.components

import androidx.compose.foundation.background
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import com.example.listi.ui.theme.ListiTheme
import com.example.listi.ui.theme.Typography
import com.example.listi.ui.theme.White
import com.example.listi.ui.types.Product
import com.example.listi.ui.types.User

@Composable
fun ProductCard(
    product: Product,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp), // margen exterior para que se vea la sombra
        shape = RoundedCornerShape(dimensionResource(R.dimen.medium_radius)),
        shadowElevation = 6.dp, // 🔥 sombra real
        tonalElevation = 2.dp, // opcional: da un pequeño relieve
        color = MaterialTheme.colorScheme.surface // color base del fondo
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(dimensionResource(R.dimen.medium_radius))
                )
                .padding(dimensionResource(R.dimen.medium_padding))
        ) {
            Spacer(Modifier.weight(1f))

        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductCardPreview() {
//    ListiTheme {
//        ProductCard(
//            "Lista Resi",
//            arrayOf(user1, user2, user3),
//            modifier = Modifier.padding(10.dp)
//        )
//    }
}
