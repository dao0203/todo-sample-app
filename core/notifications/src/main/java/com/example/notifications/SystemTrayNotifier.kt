package com.example.notifications

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.model.Todo
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

private const val EXPIRING_TODO_NOTIFICATION_ID = 1
private const val EXPIRING_TODO_NOTIFICATION_CHANNEL_ID = "ExpiringTodos"

class SystemTrayNotifier @Inject internal constructor(
    @ApplicationContext private val context: Context
) : Notifier {
    override fun postExpiringTodoNotification(todo: Todo) = with(context) {
        if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PERMISSION_GRANTED) {
            Log.i("SystemTrayNotifier", "Permission not granted")
            return
        }

        val notification = createTodoNotification {
            setContentTitle("Todo expiring soon")
                .setContentText(todo.title)
                .setStyle(NotificationCompat.BigTextStyle().bigText(todo.title))
                .setSmallIcon(com.example.common.R.drawable.ic_notification)
                .setGroupSummary(true)
            priority = NotificationCompat.PRIORITY_DEFAULT
            setAutoCancel(true)
        }

        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.notify(EXPIRING_TODO_NOTIFICATION_ID, notification)
    }

}

private fun Context.createTodoNotification(
    block: NotificationCompat.Builder.() -> Unit
): Notification {
    ensureNotificationChannelExists()

    return NotificationCompat.Builder(this, EXPIRING_TODO_NOTIFICATION_CHANNEL_ID)
        .apply(block)
        .build()
}

private fun Context.ensureNotificationChannelExists() {
    val channel = NotificationChannel(
        EXPIRING_TODO_NOTIFICATION_CHANNEL_ID,
        "Expiring Todos",
        NotificationManager.IMPORTANCE_HIGH
    ).apply {
        description = "Notifies you when a todo is about to expire"
    }

    NotificationManagerCompat.from(this).createNotificationChannel(channel)
}