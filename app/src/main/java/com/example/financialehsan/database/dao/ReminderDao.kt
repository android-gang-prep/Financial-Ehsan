package com.example.financialehsan.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.financialehsan.database.entities.Budget
import com.example.financialehsan.database.entities.Cost
import com.example.financialehsan.database.entities.Reminder
import com.example.financialehsan.database.entities.Revenue
import com.example.financialehsan.database.entities.relations.BudgetWithCategory
import com.example.financialehsan.database.entities.relations.CostWithCategory
import com.example.financialehsan.database.entities.relations.RevenueWithCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao {
    @Insert
    fun addReminder(reminder: Reminder):Long

    @Update
    fun updateReminder(reminder: Reminder)

    @Query("SELECT * FROM reminders")
    fun getReminders():Flow<List<Reminder>>
    @Query("SELECT * FROM reminders WHERE id=:id")
    fun getReminder(id:Int):Reminder?

    @Delete
    fun deleteReminder(reminder: Reminder)
}