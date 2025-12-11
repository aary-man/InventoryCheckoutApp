package com.example.inventory

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.example.inventory.navigation.InventoryNavGraph
import com.example.inventory.ui.InventoryViewModel
import com.example.inventory.ui.InventoryViewModelFactory

class MainActivity : ComponentActivity() {

    private val inventoryViewModel: InventoryViewModel by viewModels {
        InventoryViewModelFactory(application as InventoryApplication)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Surface(color = MaterialTheme.colorScheme.background) {
                InventoryNavGraph(inventoryViewModel = inventoryViewModel)
            }
        }
    }
}