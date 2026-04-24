package com.example.rtrpproject

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.util.Calendar

object NotificationScheduler {

    fun scheduleMedicineReminder(
        context: Context,
        medicineName: String,
        timeText: String,
        requestCode: Int
    ) {
        val parsedTime = parseTime(timeText) ?: return

        val hour = parsedTime.first
        val minute = parsedTime.second

        val intent = Intent(context, MedicineReminderReceiver::class.java).apply {
            putExtra("medicine_name", medicineName)
            putExtra("medicine_time", timeText)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            if (before(Calendar.getInstance())) {
                add(Calendar.DAY_OF_MONTH, 1)
            }
        }

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (alarmManager.canScheduleExactAlarms()) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        } else {
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        }
    }

    private fun parseTime(timeText: String): Pair<Int, Int>? {
        return try {
            val cleaned = timeText.trim().uppercase()
            val isPm = cleaned.contains("PM")
            val isAm = cleaned.contains("AM")
            val numberPart = cleaned
                .replace("AM", "")
                .replace("PM", "")
                .trim()

            val parts = numberPart.split(":")
            var hour = parts[0].toInt()
            val minute = if (parts.size > 1) parts[1].toInt() else 0

            if (isPm && hour != 12) hour += 12
            if (isAm && hour == 12) hour = 0

            Pair(hour, minute)
        } catch (e: Exception) {
            null
        }
    }
}