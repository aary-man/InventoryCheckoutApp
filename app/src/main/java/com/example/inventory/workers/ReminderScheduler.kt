package com.example.inventory.workers

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import java.util.concurrent.TimeUnit

object ReminderScheduler {

    fun scheduleRemindersForItem(
        context: Context,
        itemId: Int,
        itemName: String,
        userName: String,
        borrowTime: Long
    ) {
        val workManager = WorkManager.getInstance(context)

        val commonData = workDataOf(
            ReminderWorker.KEY_ITEM_ID to itemId,
            ReminderWorker.KEY_ITEM_NAME to itemName,
            ReminderWorker.KEY_USER_NAME to userName,
            ReminderWorker.KEY_BORROW_TIME to borrowTime
        )

        val work24h = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(24, TimeUnit.HOURS)
            .setInputData(
                workDataOf(
                    *commonData.keyValueMap.entries.map { it.key to it.value }.toTypedArray(),
                    ReminderWorker.KEY_REMINDER_TYPE to "24h"
                )
            )
            .addTag(tagForItem(itemId))
            .build()

        val work72h = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(72, TimeUnit.HOURS)
            .setInputData(
                workDataOf(
                    *commonData.keyValueMap.entries.map { it.key to it.value }.toTypedArray(),
                    ReminderWorker.KEY_REMINDER_TYPE to "72h"
                )
            )
            .addTag(tagForItem(itemId))
            .build()

        val periodic7d = PeriodicWorkRequestBuilder<ReminderWorker>(
            7, TimeUnit.DAYS
        )
            .setInputData(
                workDataOf(
                    *commonData.keyValueMap.entries.map { it.key to it.value }.toTypedArray(),
                    ReminderWorker.KEY_REMINDER_TYPE to "7d"
                )
            )
            .addTag(tagForItem(itemId))
            .build()

        workManager.enqueueUniqueWork(
            "reminder_item_${'$'}itemId_24h",
            ExistingWorkPolicy.REPLACE,
            work24h
        )

        workManager.enqueueUniqueWork(
            "reminder_item_${'$'}itemId_72h",
            ExistingWorkPolicy.REPLACE,
            work72h
        )

        workManager.enqueueUniquePeriodicWork(
            "reminder_item_${'$'}itemId_7d",
            ExistingPeriodicWorkPolicy.UPDATE,
            periodic7d
        )
    }

    fun cancelRemindersForItem(context: Context, itemId: Int) {
        val workManager = WorkManager.getInstance(context)
        workManager.cancelAllWorkByTag(tagForItem(itemId))
    }

    private fun tagForItem(itemId: Int): String = "item_${'$'}itemId"
}