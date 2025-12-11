package com.example.inventory.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class ReminderWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val itemName = inputData.getString(KEY_ITEM_NAME) ?: "Unknown item"
        val userName = inputData.getString(KEY_USER_NAME) ?: "Unknown user"
        val borrowTime = inputData.getLong(KEY_BORROW_TIME, 0L)

        sendReminderEmailStub(itemName, userName, borrowTime)

        return Result.success()
    }

    private fun sendReminderEmailStub(itemName: String, userName: String, borrowTime: Long) {
        Log.d("ReminderWorker", "Stub: send reminder email for '$itemName' borrowed by $userName at $borrowTime")
        // Replace this stub with real email sending logic.
    }

    companion object {
        const val KEY_ITEM_ID = "item_id"
        const val KEY_ITEM_NAME = "item_name"
        const val KEY_USER_NAME = "user_name"
        const val KEY_BORROW_TIME = "borrow_time"
        const val KEY_REMINDER_TYPE = "reminder_type"
    }
}