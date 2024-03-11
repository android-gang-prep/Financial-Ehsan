package com.example.financialehsan.viewModels

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financialehsan.database.entities.Cost
import com.example.financialehsan.database.entities.CostCategory
import com.example.financialehsan.database.entities.Reminder
import com.example.financialehsan.receivers.ReminderReceiver
import com.example.financialehsan.receivers.limitString
import com.example.financialehsan.repositories.CostCategoryRepository
import com.example.financialehsan.repositories.CostRepository
import com.example.financialehsan.repositories.ReminderRepository
import com.example.financialehsan.utils.AlarmUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.Calendar

class ReminderViewModel:ViewModel(),KoinComponent {
    private val reminderRepo:ReminderRepository by inject()
    private val costRepo:CostRepository by inject()
    private val categoryRepo:CostCategoryRepository by inject()

    private val _reminders = MutableStateFlow(emptyList<Reminder>())
    val reminders = _reminders.asStateFlow()




    init {
        getReminders()
    }

    fun getReminders(){
        viewModelScope.launch(Dispatchers.IO){
            reminderRepo.getReminders().collect{result->
                _reminders.update { result }
            }
        }
    }
    fun deleteReminder(reminder: Reminder,alarmManager: AlarmManager,context: Context){
        viewModelScope.launch(Dispatchers.IO){
            reminderRepo.deleteReminder(reminder)
        }
    }

    fun addReminder(reminder: Reminder,alarmManager: AlarmManager,context: Context,shouldRequestPermission:()->Unit){
        viewModelScope.launch(Dispatchers.IO) {
            if (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    alarmManager.canScheduleExactAlarms()
                } else {
                   true
                }
            ){

                reminderRepo.addReminder(reminder).also {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        reminder.nextPayTime,
                        AlarmUtils.buildPendingIntent(context,it.toInt())
                    )

                }

            }else{
                shouldRequestPermission()
            }

        }
    }
    fun payReminder(reminder: Reminder){
        val reminderCategoryTitle = "یادآور ها"
        viewModelScope.launch(Dispatchers.IO){
            val reminderCategoryId:Int

            val latestCategories = categoryRepo.getLatestCategories()
            val isReminderCategoryExists = latestCategories.any { it.title == reminderCategoryTitle }
            if (!isReminderCategoryExists){
                reminderCategoryId = categoryRepo.addCategory(CostCategory(title = reminderCategoryTitle)).toInt()
            }else{
                reminderCategoryId = latestCategories.find { it.title == reminderCategoryTitle }?.id ?: 0
            }
            costRepo.addCost(Cost(
                amount = reminder.amount,
                categoryId = reminderCategoryId,
                description = "پرداخت به مبلغ ${reminder.amount} برای یادآور ${reminder.description.limitString(50)}",
            ))
            val nextTimeToPay = reminder.getNextTimeToPay()
            updateReminder(reminder = reminder.copy(
                nextPayTime = nextTimeToPay
            ))
        }
    }
    fun updateReminder(reminder: Reminder){
        viewModelScope.launch(Dispatchers.IO) {
            reminderRepo.updateReminder(reminder)
        }
    }
}