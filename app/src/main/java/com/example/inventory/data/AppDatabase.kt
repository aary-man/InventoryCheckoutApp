package com.example.inventory.data

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import java.util.concurrent.Executors

@Database(
    entities = [
        ItemEntity::class,
        BorrowRecordEntity::class
    ],
    version = 2,
    exportSchema = false
)

@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun itemDao(): ItemDao
    abstract fun borrowHistoryDao(): BorrowHistoryDao


    companion object {
        fun createPrepopulateCallback(): Callback {
            return object : Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    Executors.newSingleThreadExecutor().execute {
                        try {
                            prepopulate(db)
                        } catch (t: Throwable) {
                            Log.e("AppDatabase", "Error prepopulating DB", t)
                        }
                    }
                }

                private fun prepopulate(db: SupportSQLiteDatabase) {
                    insertItem(
                        db,
                        sku = "PH-001",
                        name = "Demo Printhead A",
                        serial = "PH-A-123456",
                        location = "Shelf A1",
                        category = ItemCategory.PRINTHEAD
                    )
                    insertItem(
                        db,
                        sku = "CTL-010",
                        name = "Controller Box 1",
                        serial = "CTL-010-ABC",
                        location = "Cabinet C2",
                        category = ItemCategory.CONTROLLER
                    )
                    insertItem(
                        db,
                        sku = "MSC-900",
                        name = "Misc Cable Bundle",
                        serial = "MSC-900-Z",
                        location = "Drawer D4",
                        category = ItemCategory.MISC
                    )
                }

                private fun insertItem(
                    db: SupportSQLiteDatabase,
                    sku: String,
                    name: String,
                    serial: String,
                    location: String,
                    category: ItemCategory
                ) {
                    val values = ContentValues().apply {
                        put("sku", sku)
                        put("name", name)
                        put("serialNumber", serial)
                        put("location", location)
                        put("lastBorrowedTimestamp", 0L)
                        put("lastBorrowedBy", "")
                        put("category", category.name)
                    }
                    db.insert("items", SQLiteDatabase.CONFLICT_REPLACE, values)
                }
            }
        }
    }
}