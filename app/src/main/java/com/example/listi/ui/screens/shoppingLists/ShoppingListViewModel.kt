package com.example.listi.ui.screens.shoppingLists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.listi.network.RetrofitInstance
import com.example.listi.repository.ShoppingListRepository
import com.example.listi.ui.types.ShoppingListRequest
import com.example.listi.ui.types.ShoppingList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class ShoppingListsViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val shoppingListService = RetrofitInstance.shoppingListService
        return ShoppingListsViewModel(
            shoppingListsRespository = ShoppingListRepository(shoppingListService)
        ) as T
    }
}

class ShoppingListsViewModel(
    private val shoppingListsRespository: ShoppingListRepository,
) : ViewModel() {

    private val _shoppingLists = MutableStateFlow<MutableList<ShoppingList>>(mutableListOf())
    val shoppingLists = _shoppingLists.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    fun loadShoppingLists() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // TODO: Deberian ser todas MutableList para que sea reactivo??
                _shoppingLists.value = shoppingListsRespository.getShoppingLists(false) as MutableList<ShoppingList>

            } catch (e: Exception) {
                println("ERRROOOROROROROROROROOROR:")
                println(e.localizedMessage)
                _errorMessage.value = e.localizedMessage
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createShoppingLists(shoppingList: ShoppingListRequest) {
        viewModelScope.launch {
            try {
                shoppingListsRespository.createShoppingList(
                    request = shoppingList
                )
                loadShoppingLists()
            } catch (_: Exception) {}
        }
    }

    fun updateShoppingLists(shoppingList: ShoppingList) {
        viewModelScope.launch {
            try {
                shoppingListsRespository.updateShoppingList(
                    id = shoppingList.id,
                    request = ShoppingListRequest(
                        shoppingList.name,
                        shoppingList.description,
                        shoppingList.recurring
                    )
                )
                loadShoppingLists()
            } catch (_: Exception) {}
        }
    }

    fun deleteShoppingLists(id: Int) {
        viewModelScope.launch {
            try {
                shoppingListsRespository.deleteShoppingList(id)
                loadShoppingLists()
            } catch (_: Exception) {}
        }
    }

}