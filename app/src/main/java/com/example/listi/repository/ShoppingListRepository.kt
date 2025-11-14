package com.example.listi.repository

import android.util.Log
import android.util.Log.e
import com.example.listi.network.ShoppingListService
import com.example.listi.ui.types.ShoppingList
import com.example.listi.ui.types.CreateShoppingListRequest
import com.example.listi.ui.types.ShareShoppingListRequest
import com.example.listi.ui.types.UpdateShoppingListRequest
import com.example.listi.ui.types.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.collections.plus


class ShoppingListRepository (private val api: ShoppingListService) {

    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())

    private var cachedShoppingList: List<ShoppingList>? = null


    suspend fun getShoppingLists(forceRefresh: Boolean): List<ShoppingList> {
        return withContext(Dispatchers.IO) {
            if(cachedShoppingList != null && !forceRefresh) {
                return@withContext cachedShoppingList!!
            }

            val response = api.getShoppingLists()
            if (response.isSuccessful) {
                val shoppingLists = response.body()?.data ?: emptyList()
                cachedShoppingList = shoppingLists
                return@withContext shoppingLists
            } else {
                throw Exception("Error al obtener listas de compras: ${response.code()}")
            }
        }
    }


    suspend fun createShoppingList(request: CreateShoppingListRequest): ShoppingList {
        return try {
            val response = api.createShoppingList(request)
            if (response.isSuccessful) {
                val createdResponse = response.body()!!
                // convertimos el CategoryResponse a Category manualmente
                val created = ShoppingList(
                    id = createdResponse.id,
                    name = createdResponse.name,
                    description = createdResponse.description,
                    metadata = "",
                    recurring = createdResponse.recurring,
                    owner = createdResponse.owner,
                    sharedWith = createdResponse.sharedWith,
                    lastPurchasedAt = Date().toString(),
                    createdAt = Date().toString(),
                    updatedAt = Date().toString()
                )
                cachedShoppingList = (cachedShoppingList ?: emptyList()) + created
                created
            } else {
                throw Exception("Error al crear lista de compras: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("RegisterScreen", "Error dentro de service.createShoppingList()", e)
            throw e
        }
    }


    suspend fun getShoppingListById(id: Int): ShoppingList {
        return withContext(Dispatchers.IO) {
            val response = api.getShoppingListById(id)
            if (response.isSuccessful) {
                val result = response.body()!!
                ShoppingList(
                    id = result.id,
                    name = result.name,
                    description = result.description,
                    metadata = "",
                    recurring = result.recurring,
                    owner = result.owner,
                    sharedWith = result.sharedWith,
                    lastPurchasedAt = result.lastPurchasedAt,
                    createdAt = Date().toString(),
                    updatedAt = Date().toString()
                )
            } else {
                throw Exception("Error al obtener lista de compras: ${response.code()}")
            }
        }
    }

    suspend fun updateShoppingList(id: Int, request: UpdateShoppingListRequest): ShoppingList {
        return withContext(Dispatchers.IO) {
            val response = api.updateShoppingList(id, request)
            if (response.isSuccessful) {
                val result = response.body()!!
                val updated = ShoppingList(
                    id = result.id,
                    name = result.name,
                    description = result.description,
                    metadata = "",
                    recurring = result.recurring,
                    owner = result.owner,
                    sharedWith = result.sharedWith,
                    lastPurchasedAt = result.lastPurchasedAt,
                    createdAt = Date().toString(),
                    updatedAt = Date().toString()
                )

                // Actualiza caché
                cachedShoppingList = cachedShoppingList?.map {
                    if (it.id == id) updated else it
                }

                updated
            } else {
                throw Exception("Error al actualizar lista de compras: ${response.code()}")
            }
        }
    }

    suspend fun deleteShoppingList(id: Int) {
        return withContext(Dispatchers.IO) {
            val response = api.deleteShoppingList(id)
            if (response.isSuccessful) {
                cachedShoppingList = cachedShoppingList?.filterNot { it.id == id }
            } else {
                throw Exception("Error al eliminar lista de compras: ${response.code()}")
            }
        }
    }

    //TODO: dudosa
    suspend fun shareShoppingList(id: Int, request: ShareShoppingListRequest): ShoppingList {
        return withContext(Dispatchers.IO) {
            val response = api.shareShoppingList(request)
            if (response.isSuccessful) {
                val result = response.body()!!
                val updated = ShoppingList(
                    id = result.id,
                    name = result.name,
                    description = result.description,
                    metadata = "",
                    recurring = result.recurring,
                    owner = result.owner,
                    sharedWith = result.sharedWith,
                    lastPurchasedAt = result.lastPurchasedAt,
                    createdAt = Date().toString(),
                    updatedAt = Date().toString()
                )

                // Actualiza caché
                cachedShoppingList = cachedShoppingList?.map {
                    if (it.id == id) updated else it
                }

                updated
            } else {
                throw Exception("Error al actualizar lista de compras: ${response.code()}")
            }
        }
    }



}