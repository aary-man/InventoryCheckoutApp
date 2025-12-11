package com.example.inventory.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "borrow_history")
data class BorrowRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val itemId: Int,
    val userName: String,
    val action: BorrowAction,
    val timestamp: Long
)
