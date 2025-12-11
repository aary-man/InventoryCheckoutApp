package com.example.inventory.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventory.InventoryApplication
import com.example.inventory.data.ItemCategory
import com.example.inventory.data.ItemEntity
import com.example.inventory.data.ItemRepository
import com.example.inventory.workers.ReminderScheduler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.content.Context

class InventoryViewModel(
    application: Application,
    private val repository: ItemRepository
) : AndroidViewModel(application) {

    companion object {
        const val ADMIN_NAME = "Aryaman Himatsingka"
    }

    private val _currentUserName = MutableStateFlow<String?>(null)
    val currentUserName: StateFlow<String?> = _currentUserName.asStateFlow()

    private val _selectedCategory = MutableStateFlow<ItemCategory?>(null)
    val selectedCategory: StateFlow<ItemCategory?> = _selectedCategory.asStateFlow()

    private val _selectedItemIds = MutableStateFlow<Set<Int>>(emptySet())
    val selectedItemIds: StateFlow<Set<Int>> = _selectedItemIds.asStateFlow()

    fun loginAs(userName: String) {
        _currentUserName.value = userName
        _selectedItemIds.value = emptySet()
        _selectedCategory.value = null
    }

    fun logout() {
        _currentUserName.value = null
        _selectedItemIds.value = emptySet()
        _selectedCategory.value = null
    }

    fun isCurrentUserAdmin(): Boolean =
        _currentUserName.value == ADMIN_NAME

    fun selectCategory(category: ItemCategory) {
        _selectedCategory.value = category
        // Do NOT clear _selectedItemIds here – cart is global
    }

    fun addItemToCart(itemId: Int): Boolean {
        val current = _selectedItemIds.value
        return if (current.contains(itemId)) {
            false
        } else {
            _selectedItemIds.value = current + itemId
            true
        }
    }


    fun toggleItemSelection(itemId: Int) {
        val current = _selectedItemIds.value.toMutableSet()
        if (current.contains(itemId)) {
            current.remove(itemId)
        } else {
            current.add(itemId)
        }
        _selectedItemIds.value = current
    }

    fun clearSelection() {
        _selectedItemIds.value = emptySet()
    }

    fun itemsForCategory(category: ItemCategory) =
        repository.getItemsByCategory(category)

    fun borrowedItemsForCurrentUser() =
        repository.getBorrowedItemsForUser(_currentUserName.value ?: "")

    fun allItems() = repository.getAllItems()

    fun confirmBorrow(
        context: Context,
        allItems: List<ItemEntity>,
        onDone: () -> Unit
    ) {
        val userName = _currentUserName.value ?: return
        val selectedIds = _selectedItemIds.value
        if (selectedIds.isEmpty()) return

        val now = System.currentTimeMillis()

        viewModelScope.launch {
            allItems
                .filter { selectedIds.contains(it.id) }
                .forEach { item ->
                    val updated = item.copy(
                        lastBorrowedBy = userName,
                        lastBorrowedTimestamp = now
                    )
                    repository.updateItem(updated)
                    repository.logBorrow(updated.id, userName, now)

                    ReminderScheduler.scheduleRemindersForItem(
                        context = context,
                        itemId = updated.id,
                        itemName = updated.name,
                        userName = userName,
                        borrowTime = now
                    )
                }

            _selectedItemIds.value = emptySet()
            onDone()
        }
    }


    fun returnItems(
        context: Context,
        borrowedItems: List<ItemEntity>,
        selectedToReturn: Set<Int>,
        onDone: () -> Unit
    ) {
        val userName = _currentUserName.value ?: "Unknown"   // <-- ADD THIS LINE
        val now = System.currentTimeMillis()                 // <-- also needed
        viewModelScope.launch {
            borrowedItems
                .filter { selectedToReturn.contains(it.id) }
                .forEach { item ->
                    val updated = item.copy(
                        lastBorrowedBy = "",
                        lastBorrowedTimestamp = 0L
                    )
                    repository.updateItem(updated)
                    repository.logReturn(updated.id, userName, now)

                    ReminderScheduler.cancelRemindersForItem(context, updated.id)
                }
            onDone()
        }
    }

    fun addItem(
        sku: String,
        name: String,
        serial: String,
        location: String,
        category: ItemCategory
    ) {
        viewModelScope.launch {
            repository.addItem(
                ItemEntity(
                    sku = sku,
                    name = name,
                    serialNumber = serial,
                    location = location,
                    category = category
                )
            )
        }
    }

    fun updateItem(item: ItemEntity) {
        viewModelScope.launch {
            repository.updateItem(item)
        }
    }

    fun deleteItem(item: ItemEntity) {
        viewModelScope.launch {
            repository.deleteItem(item)
        }
    }
    fun itemHistory(itemId: Int) =
        repository.historyForItem(itemId)

}