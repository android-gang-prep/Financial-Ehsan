package com.example.financialehsan.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar


@Entity("reminders")
data class Reminder(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val description: String,
    val amount: Long,
    val dayOfMonth: Int,
    @ColumnInfo("next_pay_time")
    val nextPayTime: Long
) {
    fun getNextTimeToPay(): Long {
        val fromDay = Calendar.getInstance().apply { timeInMillis = nextPayTime }
        return getTimeToPay(fromDay.get(Calendar.DAY_OF_MONTH),addMonth = true)
    }
}

fun getTimeToPay(fromDay: Int,addMonth:Boolean = false): Long {
    val today = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    return Calendar.getInstance().apply {
        if (fromDay < today || addMonth) {
            add(Calendar.MONTH, 1)
        }
        set(Calendar.DAY_OF_MONTH, fromDay)
    }.timeInMillis
}
