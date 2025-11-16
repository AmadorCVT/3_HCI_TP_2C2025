package com.example.listi.repository

import android.util.Log
import android.util.Log.e
import com.example.listi.network.ShoppingListService
import com.example.listi.ui.types.PurchaseShoppingListRequest
import com.example.listi.ui.types.ShoppingList
import com.example.listi.ui.types.ShoppingListRequest
import com.example.listi.ui.types.ShareShoppingListRequest
import com.example.listi.ui.types.ShoppingListResponse
import com.example.listi.ui.types.ShoppingListSharedResponse
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
                throw Exception(response.code().toString())
            }
        }
    }


    suspend fun createShoppingList(request: ShoppingListRequest): ShoppingList {
        return withContext(Dispatchers.IO) {
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
                throw Exception(response.code().toString())
            }
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
                throw Exception(response.code().toString())
            }
        }
    }

    suspend fun purchaseShoppingList(id: Int): ShoppingList {
        return withContext(Dispatchers.IO) {
            val response = api.purchaseShoppingList(id, PurchaseShoppingListRequest(""))
            if (response.isSuccessful) {
                val result = response.body()!!
                result
            } else {
                throw Exception(response.code().toString())
            }
        }
    }

    suspend fun resetShoppingList(id: Int): ShoppingList {
        return withContext(Dispatchers.IO) {
            val response = api.resetShoppingList(id)
            if (response.isSuccessful) {
                val result = response.body()!!
                result
            } else {
                throw Exception(response.code().toString())
            }
        }
    }

    suspend fun getSharedShoppingList(id: Int): List<User> {
        return withContext(Dispatchers.IO) {
            val response = api.getSharedShoppingList(id)
            if (response.isSuccessful) {
                val result = response.body()!!
                result
            } else {
                throw Exception(response.message())
            }
        }
    }

    suspend fun updateShoppingList(id: Int, request: ShoppingListRequest): ShoppingList {
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
                throw Exception(response.code().toString())
            }
        }
    }

    suspend fun deleteShoppingList(id: Int) {
        return withContext(Dispatchers.IO) {
            val response = api.deleteShoppingList(id)
            if (response.isSuccessful) {
                cachedShoppingList = cachedShoppingList?.filterNot { it.id == id }
            } else {
                throw Exception(response.code().toString())
            }
        }
    }

    suspend fun shareShoppingList(id: Int, request: ShareShoppingListRequest) {
        return withContext(Dispatchers.IO) {
            val response = api.shareShoppingList(id, request)
            if (!response.isSuccessful) {
                throw Exception(response.message())
            }
        }
    }

    suspend fun removeShareShoppingList(id: Int, userId: Int){
        return withContext(Dispatchers.IO) {
            val response = api.removeShareShoppingList(id, userId)
            if (!response.isSuccessful) {
                throw Exception(response.message())
            }
        }
    }



}