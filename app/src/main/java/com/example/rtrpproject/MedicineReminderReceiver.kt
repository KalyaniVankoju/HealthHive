package com.example.rtrpproject

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class MedicineReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val medicineName = intent.getStringExtra("medicine_name") ?: "Medicine"
        val medicineTime = intent.getStringExtra("medicine_time") ?: ""

        createNotificationChannel(context)

        val notification = NotificationCompat.Builder(context, "medicine_channel")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Medicine Reminder")
            .setContentText("Take $medicineName at $medicineTime")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context)
            .notify(medicineName.hashCode(), notification)
    }

    private fun createNotificationChannel(context: Context) {
        val channel = NotificationChannel(
            "medicine_channel",
            "Medicine Reminders",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Notifications for medicine reminders"
        }

        val manager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }
}