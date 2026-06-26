package com.petcare.app.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.petcare.app.data.local.entity.ReminderEntity
import java.text.SimpleDateFormat
import java.util.*

class ReminderScheduler(private val context: Context) {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    companion object {
        const val ACTION_SHOW_REMINDER = "com.petcare.app.action.SHOW_REMINDER"
    }

    fun scheduleReminder(reminder: ReminderEntity) {
        if (reminder.id == 0) {
            Log.e("ReminderDiag", "ERROR: No se puede programar un recordatorio con ID 0")
            return
        }

        val dateStr = reminder.reminderDate // "dd/MM/yyyy"
        val timeStr = reminder.reminderTime ?: "09:00" // "HH:mm"

        Log.d("ReminderDiag", "--- Programando Alarma ---")
        Log.d("ReminderDiag", "ID: ${reminder.id} | Titulo: ${reminder.title}")

        try {
            val dateParts = dateStr.split("/")
            val timeParts = timeStr.split(":")

            val calendar = Calendar.getInstance() 
            calendar.set(Calendar.DAY_OF_MONTH, dateParts[0].toInt())
            calendar.set(Calendar.MONTH, dateParts[1].toInt() - 1)
            calendar.set(Calendar.YEAR, dateParts[2].toInt())
            calendar.set(Calendar.HOUR_OF_DAY, timeParts[0].toInt())
            calendar.set(Calendar.MINUTE, timeParts[1].toInt())
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)

            val eventTime = calendar.timeInMillis
            val anticipationMs = reminder.anticipationMinutes * 60 * 1000L
            val triggerTime = eventTime - anticipationMs
            
            val currentTime = System.currentTimeMillis()

            if (triggerTime <= currentTime) {
                Log.w("ReminderDiag", "AVISO: El triggerTime ya pasó (${SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(triggerTime))}). No se programará.")
                return
            }

            val intent = Intent(context, ReminderReceiver::class.java).apply {
                action = ACTION_SHOW_REMINDER
                putExtra("reminder_id", reminder.id)
                putExtra("title", reminder.title)
                putExtra("description", reminder.description ?: "Tienes un recordatorio de PetCare")
            }

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                reminder.id,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
                    Log.d("ReminderDiag", "SUCCESS: Alarma EXACTA programada para: ${Date(triggerTime)}")
                } else {
                    Log.w("ReminderDiag", "Sin permiso de alarma exacta, usando setAndAllowWhileIdle")
                    alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
                }
            } else {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
                Log.d("ReminderDiag", "SUCCESS: Alarma programada para: ${Date(triggerTime)}")
            }

        } catch (e: Exception) {
            Log.e("ReminderDiag", "ERROR al parsear fecha/hora: ${e.message}")
        }
    }

    fun cancelReminder(reminder: ReminderEntity) {
        val intent = Intent(context, ReminderReceiver::class.java).apply {
            action = ACTION_SHOW_REMINDER
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            reminder.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }
}
