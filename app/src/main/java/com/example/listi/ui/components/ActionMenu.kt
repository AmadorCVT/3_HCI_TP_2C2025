package com.example.listi.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class ActionItem(
    val label: String,
    val onClick: () -> Unit
)

@Composable
fun ActionMenu(
    actions: List<ActionItem>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(Color(0xFFAEC896), shape = RoundedCornerShape(6.dp))
            .width(150.dp)
            .wrapContentHeight()
            .padding(vertical = 4.dp)
    ) {
        actions.forEachIndexed { index, action ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { action.onClick() }
                    .padding(vertical = 12.dp, horizontal = 16.dp)
            ) {
                Text(
                    text = action.label,
                    fontSize = 16.sp,
                    color = Color.Black,
                    modifier = Modifier.align(Alignment.CenterStart)
                )
            }

            // línea divisoria (excepto en el último)
            if (index != actions.lastIndex) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color(0xFF6A8F5C))
                )
            }
        }
    }
}
