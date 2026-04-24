package com.example.rtrpproject

import android.app.*
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.*
import java.util.Calendar

class StepForegroundService : Service() {

    private lateinit var stepDetectorManager: StepDetectorManager
    private lateinit var repository: StepRepository

    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private var todaySteps = 0
    private val notificationId = 1
    private val channelId = "step_tracking_channel"

    override fun onCreate() {
        super.onCreate()

        val stepDao = AppDatabase.getDatabase(applicationContext).stepDao()
        repository = StepRepository(stepDao)

        createNotificationChannel()

        serviceScope.launch {
            val today = getTodayDate()
            val existing = repository.getStepEntryByDate(today)
            todaySteps = existing?.steps ?: 0

            startForeground(notificationId, createNotification(todaySteps))
        }

        stepDetectorManager = StepDetectorManager(this) {
            serviceScope.launch {
                val today = getTodayDate()
                val existing = repository.getStepEntryByDate(today)

                val updatedSteps = (existing?.steps ?: 0) + 1
                todaySteps = updatedSteps

                repository.insertOrUpdateStep(
                    StepEntry(
                        date = today,
                        steps = updatedSteps
                    )
                )

                updateNotification(todaySteps)
            }
        }

        stepDetectorManager.startListening()
    }

    override fun onDestroy() {
        super.onDestroy()
        stepDetectorManager.stopListening()
        serviceScope.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun updateNotification(steps: Int) {
        val manager = getSystemService(NotificationManager::class.java)
        manager.notify(notificationId, createNotification(steps))
    }

    private fun createNotification(steps: Int): Notification {
        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("HealthHive Step Tracking")
            .setContentText("Today's steps: $steps")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .build()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            channelId,
            "Step Tracking",
            NotificationManager.IMPORTANCE_LOW
        )

        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }
}