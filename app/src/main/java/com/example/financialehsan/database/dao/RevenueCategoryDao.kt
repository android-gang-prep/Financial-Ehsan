package com.example.financialehsan.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.financialehsan.database.entities.RevenueCategory
import kotlinx.coroutines.flow.Flow


@Dao
interface RevenueCategoryDao {
    @Query("SELECT * FROM revenue_categories")
    fun getCategories():Flow<List<RevenueCategory>>

    @Query("SELECT * FROM revenue_categories")
    fun getLatestCategories():List<RevenueCategory>

    @Insert
    fun addCategory(category:RevenueCategory)

    @Insert
    fun addCategories(categories:List<RevenueCategory>)

    @Delete
    fun deleteCategory(category:RevenueCategory)
}