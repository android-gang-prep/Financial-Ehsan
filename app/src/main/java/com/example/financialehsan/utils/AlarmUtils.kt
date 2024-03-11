package com.example.financialehsan.utils

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.financialehsan.receivers.ReminderReceiver

class AlarmUtils {
    companion object{
        const val REMINDER_ID = "reminder_id"
        fun getTriggerInDebugMode(): Long {
            return System.currentTimeMillis()+(60*1000)
        }
        fun buildPendingIntent(context: Context,requestCode:Int):PendingIntent{
            val intent = Intent(context, ReminderReceiver::class.java)
            intent.putExtra(REMINDER_ID,requestCode)
            return PendingIntent.getBroadcast(context,requestCode,intent,
                PendingIntent.FLAG_IMMUTABLE
            )
        }
    }
}