package com.example.inventory.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.material3.*
import androidx.compose.runtime.*

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

import com.example.inventory.data.ItemCategory
import com.example.inventory.data.ItemEntity
import com.example.inventory.ui.InventoryViewModel

import kotlinx.coroutines.flow.Flow



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    allItemsFlow: kotlinx.coroutines.flow.Flow<List<ItemEntity>>,
    inventoryViewModel: InventoryViewModel,
    onLogout: () -> Unit
) {
    val items by allItemsFlow.collectAsState(initial = emptyList())

    var selectedItem by remember { mutableStateOf<ItemEntity?>(null) }
    var showHistoryForId by remember { mutableStateOf<Int?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp)
    ) {
        // ------- TOP BAR: TITLE + BACK BUTTON -------
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Admin — MSSC Inventory Tracker",
                style = MaterialTheme.typography.headlineMedium
            )

            TextButton(onClick = onLogout) {
                Text("Back")
            }
        }

        Text(
            "Manage all items, update details, and view usage history.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 20.dp)
        )


        // ------- FORM PANEL -------
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    if (selectedItem == null) "Add New Item" else "Edit Item",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                var sku by remember { mutableStateOf(selectedItem?.sku ?: "") }
                var name by remember { mutableStateOf(selectedItem?.name ?: "") }
                var serial by remember { mutableStateOf(selectedItem?.serialNumber ?: "") }
                var location by remember { mutableStateOf(selectedItem?.location ?: "") }
                var category by remember { mutableStateOf(selectedItem?.category ?: ItemCategory.PRINTHEAD) }

                OutlinedTextField(
                    value = sku,
                    onValueChange = { sku = it },
                    label = { Text("SKU") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Item Name") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = serial,
                    onValueChange = { serial = it },
                    label = { Text("Serial Number") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Location") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                )

                // Dropdown for category
                Spacer(modifier = Modifier.height(4.dp))

                val categoryOptions = listOf(
                    ItemCategory.PRINTHEAD to "Printhead",
                    ItemCategory.CONTROLLER to "Controller",
                    ItemCategory.MISC to "Miscellaneous",
                    ItemCategory.HANDHELD_STANDALONE to "Handheld/Standalone"
                )

                var categoryMenuExpanded by remember { mutableStateOf(false) }

                ExposedDropdownMenuBox(
                    expanded = categoryMenuExpanded,
                    onExpandedChange = { categoryMenuExpanded = !categoryMenuExpanded }
                ) {
                    OutlinedTextField(
                        value = categoryOptions.firstOrNull { it.first == category }?.second ?: "",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Category") },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryMenuExpanded)
                        }
                    )
                    ExposedDropdownMenu(
                        expanded = categoryMenuExpanded,
                        onDismissRequest = { categoryMenuExpanded = false }
                    ) {
                        categoryOptions.forEach { (cat, label) ->
                            DropdownMenuItem(
                                text = { Text(label) },
                                onClick = {
                                    category = cat
                                    categoryMenuExpanded = false
                                }
                            )
                        }
                    }
                }


                Spacer(Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Button(
                        onClick = {
                            inventoryViewModel.addItem(sku, name, serial, location, category)
                            selectedItem = null
                        },
                        modifier = Modifier.weight(1f).padding(end = 8.dp)
                    ) {
                        Text("Add")
                    }

                    Button(
                        enabled = selectedItem != null,
                        onClick = {
                            selectedItem?.let {
                                val updated = it.copy(
                                    sku = sku,
                                    name = name,
                                    serialNumber = serial,
                                    location = location,
                                    category = category
                                )
                                inventoryViewModel.updateItem(updated)
                                selectedItem = null
                            }
                        },
                        modifier = Modifier.weight(1f).padding(end = 8.dp)
                    ) {
                        Text("Save")
                    }

                    Button(
                        enabled = selectedItem != null,
                        onClick = {
                            selectedItem?.let { inventoryViewModel.deleteItem(it) }
                            selectedItem = null
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Delete")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ------- LIST HEADER -------
        Text(
            "Inventory Items",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // ------- ITEM LIST -------
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(items) { item ->
                AdminItemCard(
                    item = item,
                    onEdit = { selectedItem = item },
                    onHistory = { showHistoryForId = item.id }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        // ------- HISTORY DIALOG -------
        showHistoryForId?.let { itemId ->
            val history = inventoryViewModel.itemHistory(itemId).collectAsState(initial = emptyList())
            ItemHistoryDialog(
                itemName = items.firstOrNull { it.id == itemId }?.name ?: "Item",
                history = history.value,
                onDismiss = { showHistoryForId = null }
            )
        }
    }
}

@Composable
private fun CategoryButton(label: String, selected: Boolean, onClick: () -> Unit) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(label) }
    )
}

@Composable
private fun AdminItemCard(
    item: ItemEntity,
    onEdit: () -> Unit,
    onHistory: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onEdit),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(item.name, style = MaterialTheme.typography.titleMedium)
                    Text("${item.sku} | ${item.serialNumber}", style = MaterialTheme.typography.bodySmall)
                    Text("Location: ${item.location}", style = MaterialTheme.typography.bodySmall)
                }

                TextButton(onClick = onHistory) {
                    Text("History")
                }
            }

            if (item.lastBorrowedBy.isNotEmpty()) {
                Text(
                    "Borrowed by ${item.lastBorrowedBy}",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }
        }
    }
}
