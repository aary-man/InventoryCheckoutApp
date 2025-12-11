package com.example.inventory.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.inventory.data.ItemEntity
import com.example.inventory.ui.InventoryHeader
import com.example.inventory.ui.InventoryViewModel
import kotlinx.coroutines.flow.Flow

@Composable
fun ReturnItemsScreen(
    userName: String,
    borrowedItemsFlow: Flow<List<ItemEntity>>,
    inventoryViewModel: InventoryViewModel,
    onDone: () -> Unit
) {
    val borrowedItems by borrowedItemsFlow.collectAsState(initial = emptyList())
    val context = LocalContext.current

    var selectedToReturn by remember { mutableStateOf<Set<Int>>(emptySet()) }

    fun toggle(id: Int) {
        val mutable = selectedToReturn.toMutableSet()
        if (mutable.contains(id)) mutable.remove(id) else mutable.add(id)
        selectedToReturn = mutable
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        InventoryHeader()
        Text(
            text = "Items borrowed by $userName",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        if (borrowedItems.isEmpty()) {
            Text(
                text = "You have no items currently checked out.",
                style = MaterialTheme.typography.bodyMedium
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items(borrowedItems) { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = item.name, style = MaterialTheme.typography.bodyLarge)
                            Text(
                                text = "SKU: ${item.sku} • SN: ${item.serialNumber}",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = "Location: ${item.location}",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        Checkbox(
                            checked = selectedToReturn.contains(item.id),
                            onCheckedChange = { toggle(item.id) }
                        )
                    }
                    Divider()
                }
            }
        }

        Button(
            onClick = {
                inventoryViewModel.returnItems(
                    context = context,
                    borrowedItems = borrowedItems,
                    selectedToReturn = selectedToReturn,
                    onDone = onDone
                )
            },
            enabled = selectedToReturn.isNotEmpty(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            Text("Confirm return")
        }

        OutlinedButton(
            onClick = onDone,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            Text("Back")
        }
    }
}