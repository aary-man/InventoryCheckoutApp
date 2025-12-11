package com.example.inventory.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object UserHome : Screen("user_home")
    object CategorySelect : Screen("category_select")
    object ItemSelect : Screen("item_select")
    object CheckoutReview : Screen("checkout_review")
    object ReturnItems : Screen("return_items")
    object Admin : Screen("admin")
}