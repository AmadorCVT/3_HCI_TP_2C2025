package com.example.listi.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.listi.R

enum class AppDestinations(
    @StringRes val label: Int,
    @DrawableRes val icon: Int,
    @StringRes val contentDescription: Int
) {
    LISTS(R.string.lists, R.drawable.list_foreground, R.string.generic_description),
    PRODUCTS(R.string.products, R.drawable.store_front_foreground, R.string.generic_description),
    PROFILE(R.string.profile, R.drawable.person_foreground, R.string.generic_description)
}