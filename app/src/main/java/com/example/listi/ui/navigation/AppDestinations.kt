package com.example.listi.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.listi.R
import kotlinx.serialization.Serializable

@Serializable
object Lists
@Serializable
object Products
@Serializable
object Friends
@Serializable
object Profile
@Serializable
object Register
@Serializable
object Login

enum class AppDestinations(
    @param:StringRes val label: Int,
    @param:DrawableRes val icon: Int,
    @param:StringRes val contentDescription: Int,
    val route: Any
) {
    LISTS(R.string.lists, R.drawable.list_foreground, R.string.generic_description, Lists),
    PRODUCTS(R.string.products, R.drawable.store_front_foreground, R.string.generic_description, Products),
    FRIENDS(R.string.friends, R.drawable.person_foreground, R.string.generic_description, Friends),
    PROFILE(R.string.profile, R.drawable.person_foreground, R.string.generic_description, Profile),
    REGISTER(R.string.registration, R.drawable.person_foreground, R.string.generic_description, Register),
    LOGIN(R.string.login,R.drawable.person_foreground,  R.string.generic_description, Login)

}
