package com.avion.alarmmanager.receiver

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.avion.alarmmanager.R
import com.avion.alarmmanager.services.AlarmService
import com.avion.alarmmanager.services.TimeService
import com.avion.alarmmanager.util.Constants
import com.avion.alarmmanager.util.RandomIntUtil
import java.util.*
import java.util.concurrent.TimeUnit

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val timeInMillis = intent.getLongExtra(Constants.EXTRA_EXACT_ALARM_TIME, 0L)
        when (intent.action) {
            Constants.ACTION_SET_EXACT_ALARM -> {

                //here you can perform any operation like start service,foreground service,
                // show notification.

                /*
                * Calling foreground service
                * */
                Intent(context, TimeService::class.java).also {
                    ContextCompat.startForegroundService(context, it)
                }
            }
            Constants.ACTION_SET_REPETITIVE_ALARM -> {
                val cal = Calendar.getInstance().apply {
                    this.timeInMillis = timeInMillis + TimeUnit.DAYS.toMillis(7)
                }
                AlarmService(context).setRepetitiveAlarm(cal.timeInMillis)
                createNotificationChannel(context)
                val notificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                notificationManager.notify(
                    RandomIntUtil.getRandomInteger(),
                    getNotification(context)
                )
            }
        }

    }


    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "channel_id",
                "Alarm Service Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )

            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun getNotification(context: Context): Notification {
        return NotificationCompat.Builder(context, "channel_id")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Weekly Alarm Notification ")
            .setContentText("One week passed since you set this alarm service.")
            .build()
    }

}