package com.example.financialehsan.repositories

import com.example.financialehsan.database.dao.BudgetDao
import com.example.financialehsan.database.dao.CostDao
import com.example.financialehsan.database.dao.ReminderDao
import com.example.financialehsan.database.dao.RevenueDao
import com.example.financialehsan.database.entities.Budget
import com.example.financialehsan.database.entities.Cost
import com.example.financialehsan.database.entities.Reminder
import com.example.financialehsan.database.entities.Revenue
import com.example.financialehsan.database.entities.relations.BudgetWithCategory
import com.example.financialehsan.database.entities.relations.CostWithCategory
import com.example.financialehsan.database.entities.relations.RevenueWithCategory
import kotlinx.coroutines.flow.Flow

class ReminderRepository(private val dao:ReminderDao) {
    fun addReminder(reminder: Reminder):Long{
        return dao.addReminder(reminder)
    }
    fun updateReminder(reminder: Reminder){
        dao.updateReminder(reminder)
    }
    fun getReminders():Flow<List<Reminder>>{
        return dao.getReminders()
    }
    fun deleteReminder(reminder: Reminder){
        return dao.deleteReminder(reminder)
    }

    fun getReminder(id:Int):Reminder?{
        return dao.getReminder(id)
    }
}