package com.example.listi.ui.screens.friends

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.listi.network.RetrofitInstance
import com.example.listi.repository.ShoppingListRepository
import com.example.listi.ui.types.Friend
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FriendsViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val shoppingListApi = RetrofitInstance.shoppingListService
        return ProductViewModel(
            shoppingListRepository = ShoppingListRepository(shoppingListApi),
        ) as T
    }
}

class ProductViewModel(
    private val shoppingListRepository: ShoppingListRepository,
) : ViewModel() {

    private val _friends = MutableStateFlow<List<Friend>>(emptyList())
    val products = _friends.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

//    fun loadFriends() {
//        viewModelScope.launch {
//            _isLoading.value = true
//            try {
//                _friends.value = friendRepository.getFriends()
//            } catch (e: Exception) {
//                _errorMessage.value = e.localizedMessage
//            } finally {
//                _isLoading.value = false
//            }
//        }
//    }
}