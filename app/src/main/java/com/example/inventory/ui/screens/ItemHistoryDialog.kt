package com.example.inventory.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.inventory.data.BorrowRecordEntity
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ItemHistoryDialog(
    itemName: String,
    history: List<BorrowRecordEntity>,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("History for $itemName") },
        text = {
            if (history.isEmpty()) {
                Text("No history available.")
            } else {
                LazyColumn(modifier = Modifier.height(300.dp)) {
                    items(history) { record ->
                        val formatted = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                            .format(Date(record.timestamp))

                        Column(modifier = Modifier.padding(vertical = 8.dp)) {
                            Text(
                                text = "${record.action} by ${record.userName}",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = formatted,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}
