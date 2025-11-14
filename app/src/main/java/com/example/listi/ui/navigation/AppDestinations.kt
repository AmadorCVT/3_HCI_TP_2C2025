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
    val route: String
) {
    LISTS(R.string.lists, R.drawable.list_foreground, R.string.generic_description, ROUTE_LISTS),
    PRODUCTS(R.string.products, R.drawable.store_front_foreground, R.string.generic_description, ROUTE_PRODUCTS),
    FRIENDS(R.string.friends, R.drawable.person_foreground, R.string.generic_description, ROUTE_FRIENDS),
    PROFILE(R.string.profile, R.drawable.person_foreground, R.string.generic_description, ROUTE_PROFILE),
    LOGIN(R.string.login, R.drawable.person_foreground, R.string.generic_description, ROUTE_LOGIN),
}
const val ROUTE_LISTS = "Shopping Lists"
const val ROUTE_PRODUCTS = "Products"
const val ROUTE_PROFILE = "Profile"
const val ROUTE_FRIENDS = "Friends"
const val ROUTE_LOGIN = "Login"
const val ROUTE_REGISTER ="Register"
const val ROUTE_VERIFY = "Verify"
const val ROUTE_PASSWORD_CODE = "Password code"
const val ROUTE_PASSWORD = "Restore password"