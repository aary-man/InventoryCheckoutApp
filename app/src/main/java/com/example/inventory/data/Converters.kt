package com.example.inventory.data

import androidx.room.TypeConverter

enum class BorrowAction {
    BORROW,
    RETURN
}

class Converters {

    @TypeConverter
    fun fromCategory(category: ItemCategory?): String? = category?.name

    @TypeConverter
    fun toCategory(value: String?): ItemCategory? =
        value?.let { ItemCategory.valueOf(it) }

    @TypeConverter
    fun fromBorrowAction(action: BorrowAction?): String? = action?.name

    @TypeConverter
    fun toBorrowAction(value: String?): BorrowAction? =
        value?.let { BorrowAction.valueOf(it) }
}
