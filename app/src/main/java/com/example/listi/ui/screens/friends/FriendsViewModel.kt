package com.example.listi.ui.screens.friends

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.listi.network.RetrofitInstance
import com.example.listi.repository.AuthRepository
import com.example.listi.repository.ShoppingListRepository
import com.example.listi.ui.screens.auth.AuthViewModel
import com.example.listi.ui.types.Friend
import com.example.listi.ui.types.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FriendsViewModelFactory(
    private val authViewModel: AuthViewModel
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val shoppingListApi = RetrofitInstance.shoppingListService
        return FriendsViewModel(
            authViewModel = authViewModel,
            shoppingListRepository = ShoppingListRepository(shoppingListApi),
        ) as T
    }
}

class FriendsViewModel(
    private val authViewModel: AuthViewModel,
    private val shoppingListRepository: ShoppingListRepository,
) : ViewModel() {

    private val _friends = MutableStateFlow<MutableList<Friend>>(mutableListOf())
    val friends = _friends.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    fun loadFriends() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val shoppingLists = shoppingListRepository.getShoppingLists(false)

                val user = authViewModel.uiState.currentUser

                for (item in shoppingLists) {
                    for (friend in item.sharedWith) {
                        if (friend.id != user?.id)
                            _friends.value.add(Friend(friend.name))
                    }
                }
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage
            } finally {
                _isLoading.value = false
            }
        }
    }
}