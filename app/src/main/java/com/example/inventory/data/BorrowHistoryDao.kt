package com.example.inventory.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BorrowHistoryDao {

    @Insert
    suspend fun insert(record: BorrowRecordEntity)

    @Query("SELECT * FROM borrow_history WHERE itemId = :itemId ORDER BY timestamp DESC")
    fun historyForItem(itemId: Int): Flow<List<BorrowRecordEntity>>
}
