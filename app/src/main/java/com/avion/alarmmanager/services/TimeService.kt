package com.avion.alarmmanager.services

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.avion.alarmmanager.R
import java.util.*

private const val TAG = "TimeService"

class TimeService : Service() {
    private var binder: IBinder = LocalTimerBinder()
    private val timer = Timer()
    private var count = 0
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate: ")
        startTimer()
    }

    private fun startTimer() {
        timer.schedule(
            object : TimerTask() {
                override fun run() {
                    if (count < 20) {
                        startTimer()
                        Log.d(TAG, "run: Timer will be called after 1 sec.")
                        count++
                    } else {
                        Log.d(TAG, "run: Explicitly calling stop self")
                        stopSelf()
                    }
                }
            }, 1000
        )
    }

    override fun onBind(intent: Intent?): IBinder {
        Log.d(TAG, "onBind: ")
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand: ")
        startForeground(123, getNotification())
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: ")
        timer.cancel()
    }

    class LocalTimerBinder : Binder() {
        fun getService(): TimeService {
            return TimeService()
        }
    }


    private fun getNotification(): Notification {
        return NotificationCompat.Builder(this, "channel_id")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Task Running in background")
            .setContentText("Log is being printed via this foreground service.")
            .build()
    }

}