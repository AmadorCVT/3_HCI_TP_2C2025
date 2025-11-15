package com.example.listi.ui.screens.shoppingLists

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.listi.network.RetrofitInstance
import com.example.listi.repository.ShoppingListItemRepository
import com.example.listi.ui.types.ShoppingListItemRequest
import com.example.listi.ui.types.ShoppingListItem
import com.example.listi.ui.types.UpdateShoppingListItemRequest
import kotlinx.coroutines.CoroutineScope
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

    private val _items = MutableStateFlow<List<ShoppingListItem>>(emptyList())
    val items = _items.asStateFlow()

    // Para decirle a la UI cuando recomponerse
    private val _refreshTrigger = MutableStateFlow(0)
    val refreshTrigger = _refreshTrigger.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    fun clearError() {
        _errorMessage.value = null
    }

    fun loadShoppingListItems(listId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _items.value = shoppingListItemRepository.getShoppingListItems(listId, true)
            } catch (e: Exception) {
                println(e.localizedMessage)
                _errorMessage.value = e.localizedMessage
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createShoppingListsItem(listId: Int, item: ShoppingListItemRequest) {
        viewModelScope.launch {
            try {
                shoppingListItemRepository.createShoppingListItem(listId, item)
                loadShoppingListItems(listId)
            } catch (_: Exception) {}
        }
        _refreshTrigger.value++
    }

    fun updateShoppingListItem(listId: Int, itemId: Int, item: UpdateShoppingListItemRequest) {
        viewModelScope.launch {
            try {
                shoppingListItemRepository.updateShoppingListItem(listId, itemId, item)
                loadShoppingListItems(listId)
            } catch (_: Exception) {

            }
        }
        _refreshTrigger.value++
    }

    fun deleteShoppingLists(listId: Int, itemId: Int) {
        viewModelScope.launch {
            try {
                shoppingListItemRepository.deleteShoppingListItem(listId, itemId)
                loadShoppingListItems(listId)
            } catch (_: Exception) {}
        }
        _refreshTrigger.value++
    }

}