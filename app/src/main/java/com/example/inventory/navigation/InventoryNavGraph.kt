package com.example.inventory.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.inventory.data.ItemCategory
import com.example.inventory.ui.InventoryViewModel
import com.example.inventory.ui.screens.AdminScreen
import com.example.inventory.ui.screens.CategorySelectScreen
import com.example.inventory.ui.screens.CheckoutReviewScreen
import com.example.inventory.ui.screens.ItemSelectScreen
import com.example.inventory.ui.screens.LoginScreen
import com.example.inventory.ui.screens.ReturnItemsScreen
import com.example.inventory.ui.screens.UserHomeScreen
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun InventoryNavGraph(
    inventoryViewModel: InventoryViewModel,
    navController: NavHostController = rememberNavController()
) {
    val currentUserName by inventoryViewModel.currentUserName.collectAsState()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onUserSelected = { userName ->
                    inventoryViewModel.loginAs(userName)
                    if (inventoryViewModel.isCurrentUserAdmin()) {
                        navController.navigate(Screen.Admin.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    } else {
                        navController.navigate(Screen.UserHome.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                }
            )
        }

        composable(Screen.UserHome.route) {
            UserHomeScreen(
                userName = currentUserName ?: "",
                onBorrowItems = { navController.navigate(Screen.CategorySelect.route) },
                onReturnItems = { navController.navigate(Screen.ReturnItems.route) },
                onLogout = {
                    inventoryViewModel.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0)
                    }
                }
            )
        }

        composable(Screen.CategorySelect.route) {
            CategorySelectScreen(
                onCategorySelected = { category ->
                    inventoryViewModel.selectCategory(category)
                    navController.navigate(Screen.ItemSelect.route)
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.ItemSelect.route) {
            val selectedCategory by inventoryViewModel.selectedCategory.collectAsState()
            val category = selectedCategory ?: ItemCategory.PRINTHEAD
            val itemsFlow = inventoryViewModel.itemsForCategory(category)
            ItemSelectScreen(
                category = category,
                itemsFlow = itemsFlow,
                inventoryViewModel = inventoryViewModel,
                onCheckout = { navController.navigate(Screen.CheckoutReview.route) },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.CheckoutReview.route) {
            val allItemsFlow = inventoryViewModel.allItems()
            CheckoutReviewScreen(
                itemsFlow = allItemsFlow,
                inventoryViewModel = inventoryViewModel,
                onConfirmed = {
                    navController.popBackStack(Screen.UserHome.route, inclusive = false)
                },
                onBack = { navController.popBackStack() }
            )
        }


        composable(Screen.ReturnItems.route) {
            val borrowedFlow = if (currentUserName != null) {
                inventoryViewModel.borrowedItemsForCurrentUser()
            } else {
                emptyFlow()
            }
            ReturnItemsScreen(
                userName = currentUserName ?: "",
                borrowedItemsFlow = borrowedFlow,
                inventoryViewModel = inventoryViewModel,
                onDone = { navController.popBackStack() }
            )
        }

        composable(Screen.Admin.route) {
            val allItemsFlow = inventoryViewModel.allItems()
            AdminScreen(
                allItemsFlow = allItemsFlow,
                inventoryViewModel = inventoryViewModel,
                onLogout = {
                    inventoryViewModel.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0)
                    }
                }
            )
        }
    }
}