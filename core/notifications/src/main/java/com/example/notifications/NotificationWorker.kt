package com.example.notifications

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.model.Todo
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject


@HiltWorker
class NotificationWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val notifier: Notifier
) : Worker(appContext, workerParams) {

    override fun doWork(): Result {
        val input = inputData.getString("data") ?: return Result.failure()

        val todo = Todo.fromDataString(input)
        notifier.postExpiringTodoNotification(todo)
        return Result.success()
    }
}
