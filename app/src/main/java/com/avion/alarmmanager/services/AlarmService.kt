package com.avion.alarmmanager.services

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.avion.alarmmanager.receiver.AlarmReceiver
import com.avion.alarmmanager.util.Constants
import com.avion.alarmmanager.util.RandomIntUtil

class AlarmService(private val context: Context) {
    private val alarmManager: AlarmManager? =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun setExactAlarm(timeMillis: Long) {
        setAlarm(
            timeMillis,
            getPendingIntent(
                getIntent().apply {
                    action = Constants.ACTION_SET_EXACT_ALARM
                    putExtra(Constants.EXTRA_EXACT_ALARM_TIME, timeMillis)
                }
            )
        )
    }

    //Every Week
    fun setRepetitiveAlarm(timeMillis: Long) {
        setAlarm(
            timeMillis,
            getPendingIntent(
                getIntent().apply {
                    action = Constants.ACTION_SET_REPETITIVE_ALARM
                    putExtra(Constants.EXTRA_EXACT_ALARM_TIME, timeMillis)
                }
            )
        )
    }

    private fun setAlarm(timeInMillis: Long, pendingIntent: PendingIntent) {
        alarmManager?.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            timeInMillis,
            pendingIntent
        )
    }

    private fun getIntent() = Intent(context, AlarmReceiver::class.java)

    private fun getPendingIntent(intent: Intent) = PendingIntent.getBroadcast(
        context,
        RandomIntUtil.getRandomInteger(),
        intent,
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )

}