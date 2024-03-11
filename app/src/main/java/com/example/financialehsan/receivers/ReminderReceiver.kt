package com.example.financialehsan.receivers

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.IntentService
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.financialehsan.MainActivity
import com.example.financialehsan.R
import com.example.financialehsan.repositories.ReminderRepository
import com.example.financialehsan.utils.AlarmUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ReminderReceiver: BroadcastReceiver(),KoinComponent {

    private val reminderRepo:ReminderRepository by inject()
    val scope = CoroutineScope(SupervisorJob()+Dispatchers.IO)

    override fun onReceive(context: Context?, intent: Intent?) {
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val reminderId = intent?.getIntExtra(AlarmUtils.REMINDER_ID,99999)

        reminderId?.let {
            scope.launch(Dispatchers.IO){

                reminderRepo.getReminder(reminderId)?.let {
                    println("reminder for ${it.description} called")
                    withContext(Dispatchers.Main){
                        val title = it.description.limitString(50)
                        dispatchNotification(context,title)
                    }
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    it.getNextTimeToPay(),
                    AlarmUtils.buildPendingIntent(context,reminderId)
                )
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun dispatchNotification(context: Context, title:String){
        val intent = Intent(context,MainActivity::class.java)
        intent.putExtra("navigateToReminders",true)
        val pendingIntent = PendingIntent.getActivity(context,99,intent,FLAG_IMMUTABLE)
        NotificationCompat.Builder(context, MainActivity.CHANNEL_NAME)
            .setSmallIcon(R.drawable.ic_clock)
            .setContentTitle("Financial")
            .setContentText("زمان پرداخت $title رسیده است.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .build().also {
                NotificationManagerCompat.from(context).notify((100..2000).random(),it)
            }
    }

}

fun String.limitString(length:Int):String{
    return if (length > 50) substring(0..50)+"..." else this
}