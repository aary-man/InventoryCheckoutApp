package com.example.inventory.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class ItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val sku: String,
    val name: String,
    val serialNumber: String,
    val location: String,
    val lastBorrowedTimestamp: Long = 0L,
    val lastBorrowedBy: String = "",
    val category: ItemCategory
)