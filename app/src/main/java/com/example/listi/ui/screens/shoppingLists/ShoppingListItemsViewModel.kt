package com.example.listi.ui.screens.shoppingLists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.listi.network.RetrofitInstance
import com.example.listi.repository.ShoppingListItemRepository
import com.example.listi.repository.ShoppingListRepository
import com.example.listi.ui.types.CreateShoppingListItemRequest
import com.example.listi.ui.types.CreateShoppingListRequest
import com.example.listi.ui.types.ShoppingList
import com.example.listi.ui.types.ShoppingListItem
import com.example.listi.ui.types.UpdateShoppingListItemRequest
import com.example.listi.ui.types.UpdateShoppingListRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ShoppingListItemsViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val shoppingListItemService = RetrofitInstance.shoppingListItemService
        return ShoppingListItemsViewModel(
            shoppingListItemRepository = ShoppingListItemRepository(shoppingListItemService)
        ) as T
    }
}

class ShoppingListItemsViewModel(
    private val shoppingListItemRepository: ShoppingListItemRepository,
) : ViewModel() {

    private val _items = MutableStateFlow<MutableList<ShoppingListItem>>(mutableListOf())
    val items = _items.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    fun loadShoppingListItems(listId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _items.value = shoppingListItemRepository.getShoppingListItems(listId) as MutableList<ShoppingListItem>
            } catch (e: Exception) {
                println("ERRROOOROROROROROROROOROR:")
                println(e.localizedMessage)
                _errorMessage.value = e.localizedMessage
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createShoppingListsItem(listId: Int, item: CreateShoppingListItemRequest) {
        viewModelScope.launch {
            try {
                shoppingListItemRepository.createShoppingListItem(listId, item)
                loadShoppingListItems(listId)
            } catch (_: Exception) {}
        }
    }

    fun updateShoppingListItem(listId: Int, itemId: Int, item: UpdateShoppingListItemRequest) {
        viewModelScope.launch {
            try {
                shoppingListItemRepository.updateShoppingListItem(listId, itemId, item)
                loadShoppingListItems(listId)
            } catch (_: Exception) {}
        }
    }

    fun deleteShoppingLists(listId: Int, itemId: Int) {
        viewModelScope.launch {
            try {
                shoppingListItemRepository.deleteShoppingListItem(listId, itemId)
                loadShoppingListItems(listId)
            } catch (_: Exception) {}
        }
    }

}