package com.example.notifications

import android.content.Context
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.model.Todo
import java.util.concurrent.TimeUnit

object NotificationScheduler {
    fun schedule(context: Context, todo: Todo) {
        val currentTime = System.currentTimeMillis()
        val delay = todo.dueDate - currentTime

        println("delay: $delay")

        if (delay > 30) {
            val inputData = Data.Builder()
                .putString("data", todo.toJson())
                .put("notification_id", todo.id)
                .build()

            val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
                .setInitialDelay(delay - 200 * 1000, TimeUnit.MILLISECONDS)
                .setInputData(inputData)
                .addTag(todo.id.toString())
                .build()

            WorkManager.getInstance(context).enqueue(workRequest)
        }
    }

    // 通知をキャンセルする
    fun cancel(context: Context, todo: Todo) {

        WorkManager.getInstance(context).cancelAllWorkByTag(todo.id.toString())
    }
}