package com.example.financialehsan.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.financialehsan.database.dao.CostCategoryDao
import com.example.financialehsan.database.dao.CostDao
import com.example.financialehsan.database.dao.RevenueCategoryDao
import com.example.financialehsan.database.entities.Cost
import com.example.financialehsan.database.entities.CostCategory
import com.example.financialehsan.database.entities.RevenueCategory


@Database(entities = [CostCategory::class,RevenueCategory::class,Cost::class], version = 5)
abstract class AppDatabase: RoomDatabase() {
    abstract fun costCategoryDao():CostCategoryDao
    abstract fun revenueCategoryDao():RevenueCategoryDao
    abstract fun costDao():CostDao
}