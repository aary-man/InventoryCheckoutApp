package com.example.inventory.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import com.example.inventory.data.ItemCategory
import com.example.inventory.data.ItemEntity
import com.example.inventory.ui.InventoryHeader
import com.example.inventory.ui.InventoryViewModel
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ItemSelectScreen(
    category: ItemCategory,
    itemsFlow: Flow<List<ItemEntity>>,
    inventoryViewModel: InventoryViewModel,
    onCheckout: () -> Unit,
    onBack: () -> Unit
) {
    val items by itemsFlow.collectAsState(initial = emptyList())
    val selectedIds by inventoryViewModel.selectedItemIds.collectAsState()

    var statusMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        InventoryHeader()

        Text(
            text = "Browse items (${category.name})",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        statusMessage?.let { msg ->
            Text(
                text = msg,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(items) { item ->
                val isBorrowed = item.lastBorrowedBy.isNotEmpty()
                val inCart = selectedIds.contains(item.id)

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .alpha(if (isBorrowed) 0.4f else 1f),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    ) {
                        // Image placeholder
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                        ) {
                            Text(
                                text = "Img",
                                style = MaterialTheme.typography.labelSmall
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            Text(
                                text = item.name,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = "Part Number: ${item.sku}",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = "Location: ${item.location}",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = formatLastBorrowed(item.lastBorrowedTimestamp),
                                style = MaterialTheme.typography.bodySmall
                            )
                            if (isBorrowed) {
                                Text(
                                    text = "Currently borrowed by ${item.lastBorrowedBy}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.error
                                )
                            } else if (inCart) {
                                Text(
                                    text = "Already in your cart",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = {
                                if (isBorrowed) {
                                    statusMessage = "Item '${item.name}' is currently borrowed and cannot be added."
                                } else {
                                    val added = inventoryViewModel.addItemToCart(item.id)
                                    statusMessage = if (added) {
                                        "Added '${item.name}' to your cart."
                                    } else {
                                        "Item '${item.name}' is already in your cart."
                                    }
                                }
                            },
                            enabled = !isBorrowed
                        ) {
                            Text("Add")
                        }
                    }
                }

                Divider()
            }
        }

        Button(
            onClick = onCheckout,
            enabled = selectedIds.isNotEmpty(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            Text("View cart / Checkout")
        }

        OutlinedButton(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            Text("Back to categories")
        }
    }
}

private fun formatLastBorrowed(timestamp: Long): String {
    if (timestamp <= 0L) return "Last borrowed: Never"
    val df = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    return "Last borrowed: " + df.format(Date(timestamp))
}
