package com.example.inventory.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.inventory.data.ItemEntity
import com.example.inventory.ui.InventoryHeader
import com.example.inventory.ui.InventoryViewModel
import kotlinx.coroutines.flow.Flow

@Composable
fun CheckoutReviewScreen(
    itemsFlow: Flow<List<ItemEntity>>,
    inventoryViewModel: InventoryViewModel,
    onConfirmed: () -> Unit,
    onBack: () -> Unit
) {
    val allItems by itemsFlow.collectAsState(initial = emptyList())
    val selectedIds by inventoryViewModel.selectedItemIds.collectAsState()
    val context = LocalContext.current

    val selectedItems = allItems.filter { selectedIds.contains(it.id) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        InventoryHeader()

        Text(
            text = "Checkout review",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        if (selectedItems.isEmpty()) {
            Text(
                text = "No items in your cart.",
                style = MaterialTheme.typography.bodyMedium
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items(selectedItems) { item ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Text(text = item.name, style = MaterialTheme.typography.bodyLarge)
                        Text(
                            text = "Part Number: ${item.sku}",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = "Location: ${item.location}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Divider()
                }
            }
        }

        Button(
            onClick = {
                inventoryViewModel.confirmBorrow(
                    context = context,
                    allItems = allItems,
                    onDone = onConfirmed
                )
            },
            enabled = selectedItems.isNotEmpty(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            Text("Confirm borrow")
        }

        OutlinedButton(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            Text("Back")
        }
    }
}
