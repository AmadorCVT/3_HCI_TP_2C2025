package com.example.listi.repository

import com.example.listi.network.ShoppingListItemService
import com.example.listi.ui.types.CreateShoppingListItemRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale
import com.example.listi.ui.types.ShoppingListItem
import com.example.listi.ui.types.UpdateShoppingListItemRequest
class ShoppingListItemRepository(private val api: ShoppingListItemService) {

    private val cachedItems: MutableMap<Int, List<ShoppingListItem>> = mutableMapOf()

    suspend fun getShoppingListItems(
        listId: Int,
        forceRefresh: Boolean = false
    ): List<ShoppingListItem> {

        return withContext(Dispatchers.IO) {
            if (!forceRefresh && cachedItems.containsKey(listId)) {
                return@withContext cachedItems[listId]!!
            }
            val response = api.getShoppingListItems()
            if (response.isSuccessful) {
                val items = response.body()?.data ?: emptyList()
                cachedItems[listId] = items
                items
            } else {
                throw Exception("Error al obtener items: ${response.code()}")
            }
        }
    }

    suspend fun createShoppingListItem(
        listId: Int,
        request: CreateShoppingListItemRequest
    ): ShoppingListItem {
        return withContext(Dispatchers.IO) {
            val response = api.createShoppingListItem(request)
            if (response.isSuccessful) {
                val result = response.body()!!
                val created = ShoppingListItem(
                    id = result.id,
                    unit = result.unit,
                    quantity = result.quantity,
                    purchased = result.purchased,
                    lastPurchasedAt = result.lastPurchasedAt,
                    createdAt = result.createdAt,
                    updatedAt = result.updatedAt,
                    product = result.product
                )
                val oldList = cachedItems[listId] ?: emptyList()
                cachedItems[listId] = oldList + created
                created
            } else {
                throw Exception("Error al crear item: ${response.code()}")
            }
        }
    }

    suspend fun getShoppingListItemById(listId: Int, itemId: Int): ShoppingListItem {
        return withContext(Dispatchers.IO) {
            val response = api.getShoppingListItemById(listId)
            if (response.isSuccessful) {
                val r = response.body()!!
                ShoppingListItem(
                    id = r.id,
                    unit = r.unit,
                    quantity = r.quantity,
                    purchased = r.purchased,
                    lastPurchasedAt = r.lastPurchasedAt,
                    createdAt = r.createdAt,
                    updatedAt = r.updatedAt,
                    product = r.product
                )
            } else {
                throw Exception("Error al obtener item: ${response.code()}")
            }
        }
    }

    suspend fun updateShoppingListItem(
        listId: Int,
        itemId: Int,
        request: UpdateShoppingListItemRequest
    ): ShoppingListItem {
        return withContext(Dispatchers.IO) {
            val response = api.updateShoppingListItem(listId, request)
            if (response.isSuccessful) {
                val r = response.body()!!
                val updated = ShoppingListItem(
                    id = r.id,
                    unit = r.unit,
                    quantity = r.quantity,
                    purchased = r.purchased,
                    lastPurchasedAt = r.lastPurchasedAt,
                    createdAt = r.createdAt,
                    updatedAt = r.updatedAt,
                    product = r.product
                )
                cachedItems[listId] = cachedItems[listId]?.map {
                    if (it.id == itemId) updated else it
                } ?: emptyList()
                updated
            } else {
                throw Exception("Error al actualizar item: ${response.code()}")
            }
        }
    }

    suspend fun deleteShoppingListItem(listId: Int, itemId: Int) {
        return withContext(Dispatchers.IO) {
            val response = api.deleteShoppingListItem(listId)
            if (response.isSuccessful) {
                cachedItems[listId] = cachedItems[listId]?.filterNot { it.id == itemId } ?: emptyList()
            } else {
                throw Exception("Error al eliminar item: ${response.code()}")
            }
        }
    }

    suspend fun toggleStatusShoppingListItem(
        listId: Int,
        itemId: Int
    ): ShoppingListItem {
        return withContext(Dispatchers.IO) {
            val response = api.toggleStatusShoppingListItem(listId, itemId)
            if (response.isSuccessful) {
                val r = response.body()!!
                val updated = ShoppingListItem(
                    id = r.id,
                    unit = r.unit,
                    quantity = r.quantity,
                    purchased = r.purchased,
                    lastPurchasedAt = r.lastPurchasedAt,
                    createdAt = r.createdAt,
                    updatedAt = r.updatedAt,
                    product = r.product
                )
                cachedItems[listId] = cachedItems[listId]?.map {
                    if (it.id == itemId) updated else it
                } ?: emptyList()
                updated
            } else {
                throw Exception("Error al cambiar estado: ${response.code()}")
            }
        }
    }
}
