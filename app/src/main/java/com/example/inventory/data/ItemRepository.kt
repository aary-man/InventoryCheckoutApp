package com.example.inventory.data

import kotlinx.coroutines.flow.Flow

class ItemRepository(
    private val itemDao: ItemDao,
    private val borrowHistoryDao: BorrowHistoryDao
) {


    fun getAllItems(): Flow<List<ItemEntity>> = itemDao.getAllItems()

    fun getItemsByCategory(category: ItemCategory): Flow<List<ItemEntity>> =
        itemDao.getItemsByCategory(category)
    fun historyForItem(itemId: Int): Flow<List<BorrowRecordEntity>> =
        borrowHistoryDao.historyForItem(itemId)

    fun getBorrowedItemsForUser(userName: String): Flow<List<ItemEntity>> =
        itemDao.getBorrowedItemsForUser(userName)

    suspend fun addItem(item: ItemEntity) {
        itemDao.insertItem(item)
    }

    suspend fun updateItem(item: ItemEntity) {
        itemDao.updateItem(item)
    }

    suspend fun deleteItem(item: ItemEntity) {
        itemDao.deleteItem(item)
    }
    suspend fun logBorrow(itemId: Int, userName: String, timestamp: Long) {
        borrowHistoryDao.insert(
            BorrowRecordEntity(
                itemId = itemId,
                userName = userName,
                action = BorrowAction.BORROW,
                timestamp = timestamp
            )
        )
    }

    suspend fun logReturn(itemId: Int, userName: String, timestamp: Long) {
        borrowHistoryDao.insert(
            BorrowRecordEntity(
                itemId = itemId,
                userName = userName,
                action = BorrowAction.RETURN,
                timestamp = timestamp
            )
        )
    }

}