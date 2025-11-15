package com.example.listi.repository

import com.example.listi.network.ShoppingListItemService
import com.example.listi.ui.types.ShoppingListItemRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale
import com.example.listi.ui.types.ShoppingListItem
import com.example.listi.ui.types.ToggleShoppingListItemRequest
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
            val response = api.getShoppingListItems(listId)
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
        request: ShoppingListItemRequest
    ): ShoppingListItem {
        return withContext(Dispatchers.IO) {
            val response = api.createShoppingListItem(listId, request)
            if (response.isSuccessful) {
                val result = response.body()!!
                val created = result.item
                val oldList = cachedItems[listId] ?: emptyList()
                cachedItems[listId] = oldList + created
                created
            } else {
                throw Exception(response.message().toString())
            }
        }
    }

    suspend fun getShoppingListItemById(listId: Int, itemId: Int): ShoppingListItem {
        return withContext(Dispatchers.IO) {
            val response = api.getShoppingListItemById(listId)
            if (response.isSuccessful) {
                val r = response.body()!!
                r.item
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
                val updated = r.item
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
            val response = api.deleteShoppingListItem(listId, itemId)
            if (response.isSuccessful) {
                cachedItems[listId] = cachedItems[listId]?.filterNot { it.id == itemId } ?: emptyList()
            } else {
                throw Exception("Error al eliminar item: ${response.code()}")
            }
        }
    }

    suspend fun toggleStatusShoppingListItem(
        listId: Int,
        itemId: Int,
        purchased: Boolean
    ): ShoppingListItem {
        return withContext(Dispatchers.IO) {
            val response = api.toggleStatusShoppingListItem(listId, itemId,
                ToggleShoppingListItemRequest(purchased))
            if (response.isSuccessful) {
                val r = response.body()!!
                val updated = r
                cachedItems[listId] = cachedItems[listId]?.map {
                    if (it.id == itemId) updated else it
                } ?: emptyList()
                updated
            } else {
                throw Exception(response.message().toString())
            }
        }
    }
}
