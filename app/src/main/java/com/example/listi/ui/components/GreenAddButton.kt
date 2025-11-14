package com.example.listi.ui.components

import android.R.attr.contentDescription
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.listi.R
import com.example.listi.ui.theme.ListiTheme

@Composable
fun GreenAddButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize(), // ocupa toda la pantalla para posicionar dentro
        contentAlignment = Alignment.BottomEnd // abajo a la derecha
    ) {
        FloatingActionButton(
            onClick = onClick,
            containerColor = MaterialTheme.colorScheme.primary,    // verde claro
            contentColor = MaterialTheme.colorScheme.onPrimary,     // ícono oscuro
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .padding(24.dp) // distancia de los bordes
                .shadow(6.dp, RoundedCornerShape(24.dp))
                .then(modifier)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.add),
                contentDescription = "Agregar lista",
                modifier
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AddListButtonPreview() {
    ListiTheme {
       GreenAddButton(onClick = {})
    }
}
