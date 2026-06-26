package com.petcare.app.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.petcare.app.data.local.entity.ReminderEntity
import java.text.SimpleDateFormat
import java.util.Locale

class ReminderScheduler(private val context: Context) {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private val dateTimeFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    fun scheduleReminder(reminder: ReminderEntity) {
        val date = reminder.reminderDate
        val time = reminder.reminderTime ?: "09:00"

        val triggerTime = try {
            dateTimeFormat.parse("$date $time")?.time ?: return
        } catch (e: Exception) {
            return
        }

        if (triggerTime <= System.currentTimeMillis()) return

        val intent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra("title", reminder.title)
            putExtra("description", reminder.description ?: "Tienes un recordatorio para tu mascota.")
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            reminder.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // SOLUCIÓN AL ERROR DE SEGURIDAD
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // En Android 12+ verificamos si podemos programar alarmas exactas
            if (alarmManager.canScheduleExactAlarms()) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    pendingIntent
                )
            } else {
                // Si no tiene permiso de alarma exacta, usamos una alarma normal (inexacta)
                // Esto evita el SecurityException y que la app se cierre
                alarmManager.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    pendingIntent
                )
            }
        } else {
            // Para versiones anteriores a Android 12 no es necesaria la verificación
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerTime,
                pendingIntent
            )
        }
    }

    fun cancelReminder(reminder: ReminderEntity) {
        val intent = Intent(context, ReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            reminder.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }
}