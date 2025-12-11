package com.example.inventory

import android.app.Application
import androidx.room.Room
import com.example.inventory.data.AppDatabase
import com.example.inventory.data.ItemRepository
import java.util.concurrent.Executors

class InventoryApplication : Application() {

    lateinit var database: AppDatabase
        private set

    lateinit var repository: ItemRepository
        private set

    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "inventory-db"
        )
            .addCallback(AppDatabase.createPrepopulateCallback())
            .fallbackToDestructiveMigration()
            .build()


        repository = ItemRepository(
            database.itemDao(),
            database.borrowHistoryDao()
        )

    }
}