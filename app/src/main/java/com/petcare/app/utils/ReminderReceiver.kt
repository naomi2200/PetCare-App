package com.petcare.app.utils

import android.Manifest
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.petcare.app.PetCareApplication
import com.petcare.app.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ReminderReceiver : BroadcastReceiver() {

    companion object {
        const val ACTION_MARK_COMPLETED = "com.petcare.app.action.MARK_COMPLETED"
        const val ACTION_SHOW_REMINDER = "com.petcare.app.action.SHOW_REMINDER"
        const val EXTRA_REMINDER_ID = "reminder_id"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        Log.d("ReminderDiag", "RECEIVER: onReceive disparado. Action: $action")

        when (action) {
            Intent.ACTION_BOOT_COMPLETED -> {
                Log.d("ReminderDiag", "RECEIVER: Detectado BOOT_COMPLETED. Reprogramando...")
                rescheduleAllReminders(context)
            }
            ACTION_MARK_COMPLETED -> {
                val reminderId = intent.getIntExtra(EXTRA_REMINDER_ID, -1)
                Log.d("ReminderDiag", "RECEIVER: Acción de marcar como completado para ID: $reminderId")
                if (reminderId != -1) {
                    markReminderAsCompleted(context, reminderId)
                }
            }
            ACTION_SHOW_REMINDER -> {
                Log.d("ReminderDiag", "RECEIVER: Mostrando notificación de recordatorio...")
                showNotification(context, intent)
            }
            else -> {
                // Fallback por si llega sin action pero con data
                if (intent.hasExtra("reminder_id")) {
                    Log.d("ReminderDiag", "RECEIVER: Action desconocido pero tiene reminder_id. Mostrando...")
                    showNotification(context, intent)
                }
            }
        }
    }

    private fun showNotification(context: Context, intent: Intent) {
        val title = intent.getStringExtra("title") ?: "Recordatorio PetCare"
        val description = intent.getStringExtra("description") ?: "Tienes un recordatorio pendiente."
        val reminderId = intent.getIntExtra("reminder_id", -1)

        Log.d("ReminderDiag", "SHOW_NOTIF: Preparando ID: $reminderId, Titulo: $title")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permission = ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
            if (permission != PackageManager.PERMISSION_GRANTED) {
                Log.e("ReminderDiag", "SHOW_NOTIF: Sin permiso POST_NOTIFICATIONS")
                return
            }
        }

        val completeIntent = Intent(context, ReminderReceiver::class.java).apply {
            action = ACTION_MARK_COMPLETED
            putExtra(EXTRA_REMINDER_ID, reminderId)
        }
        val completePendingIntent = PendingIntent.getBroadcast(
            context,
            reminderId + 1000,
            completeIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, NotificationHelper.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setAutoCancel(true)
            .addAction(R.drawable.ic_launcher_foreground, "Completar", completePendingIntent)
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(reminderId, notification)
        Log.d("ReminderDiag", "SHOW_NOTIF: Notificación enviada al sistema.")
    }

    private fun markReminderAsCompleted(context: Context, reminderId: Int) {
        val application = context.applicationContext as PetCareApplication
        val repository = application.reminderRepository
        
        CoroutineScope(Dispatchers.IO).launch {
            val reminder = repository.getReminderById(reminderId)
            reminder?.let {
                repository.updateReminder(it.copy(isCompleted = true))
                val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                manager.cancel(reminderId)
            }
        }
    }

    private fun rescheduleAllReminders(context: Context) {
        val application = context.applicationContext as PetCareApplication
        val repository = application.reminderRepository
        val scheduler = ReminderScheduler(context)

        CoroutineScope(Dispatchers.IO).launch {
            val reminders = repository.getAllReminders().first()
            reminders.forEach { reminder ->
                if (!reminder.isCompleted) {
                    scheduler.scheduleReminder(reminder)
                }
            }
        }
    }
}
