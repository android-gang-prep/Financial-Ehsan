package com.example.financialehsan.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.financialehsan.database.dao.BudgetDao
import com.example.financialehsan.database.dao.CostCategoryDao
import com.example.financialehsan.database.dao.CostDao
import com.example.financialehsan.database.dao.ReminderDao
import com.example.financialehsan.database.dao.RevenueCategoryDao
import com.example.financialehsan.database.dao.RevenueDao
import com.example.financialehsan.database.entities.Budget
import com.example.financialehsan.database.entities.Cost
import com.example.financialehsan.database.entities.CostCategory
import com.example.financialehsan.database.entities.Reminder
import com.example.financialehsan.database.entities.Revenue
import com.example.financialehsan.database.entities.RevenueCategory


@Database(
    entities = [CostCategory::class,RevenueCategory::class,Cost::class,Revenue::class,Budget::class,Reminder::class],
    version = 9,
    exportSchema = false
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun costCategoryDao():CostCategoryDao
    abstract fun revenueCategoryDao():RevenueCategoryDao
    abstract fun costDao():CostDao
    abstract fun revenueDao():RevenueDao
    abstract fun budgetDao():BudgetDao
    abstract fun reminderDao():ReminderDao
}