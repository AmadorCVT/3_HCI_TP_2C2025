package com.example.listi.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.listi.repository.CategoryRepository
import com.example.listi.repository.ProductRepository
import com.example.listi.ui.types.Category
import com.example.listi.ui.types.Product
import java.text.SimpleDateFormat
import java.util.*
import com.example.listi.ui.theme.ListiTheme
import com.example.listi.ui.types.UpdateProductRequest
import kotlinx.coroutines.launch

@Composable
fun ProductCard(
    product: Product,
    productRepository: ProductRepository,
    categoryRepository: CategoryRepository,
    onMenuClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var categories by remember { mutableStateOf<List<Category>>(emptyList()) }
    var expanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf(product.category) }

    val coroutineScope = rememberCoroutineScope()
    var isUpdating by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Cargar categorías al montar el composable
    LaunchedEffect(Unit) {
        try {
            categories = categoryRepository.getCategories()
        } catch (e: Exception) {
            // manejar error de carga (log / mostrar mensaje)
            categories = emptyList()
        }
    }

    val formattedDate = remember(product.createdAt) {
        SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(product.createdAt)
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = product.name,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Box {
                OutlinedButton(
                    onClick = { expanded = true },
                    modifier = Modifier
                        .width(110.dp)
                        .height(28.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = selectedCategory.name,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Box {
                    OutlinedButton(
                        onClick = { expanded = true },
                        modifier = Modifier.width(110.dp).height(28.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = selectedCategory.name,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        categories.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category.name, fontSize = 12.sp) },
                                onClick = {
                                    expanded = false
                                    // Lanzamos coroutine para actualizar el producto
                                    coroutineScope.launch {
                                        isUpdating = true
                                        errorMessage = null
                                        try {
                                            val updated = productRepository.updateProduct(
                                                id = product.id,
                                                request = UpdateProductRequest(
                                                    name = product.name,
                                                    category = com.example.listi.ui.types.CategoryReference(
                                                        category.id
                                                    )
                                                )
                                            )
                                            // actualizar UI localmente con la categoría retornada
                                            selectedCategory = updated.category
                                        } catch (e: Exception) {
                                            errorMessage = e.message ?: "Error al actualizar"
                                        } finally {
                                            isUpdating = false
                                        }
                                    }
                                })
                        }
                    }
                }

                if (isUpdating) {
                    // por ejemplo, un pequeño indicador
                    CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                }
                errorMessage?.let {
                    Text(text = it, color = MaterialTheme.colorScheme.error, fontSize = 11.sp)
                }
                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = formattedDate,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    fontSize = 11.sp,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.width(6.dp))

                IconButton(
                    onClick = { onMenuClick?.invoke() },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Opciones",
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}
