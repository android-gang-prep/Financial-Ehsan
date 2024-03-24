package com.example.financialehsan.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.financialehsan.database.entities.CostCategory
import com.example.financialehsan.database.entities.RevenueCategory
import kotlinx.coroutines.flow.Flow


@Dao
interface CostCategoryDao {
    @Query("SELECT * FROM cost_categories")
    fun getCategories():Flow<List<CostCategory>>

    @Query("SELECT * FROM cost_categories")
    @Transaction
    fun getLatestCategories():List<CostCategory>

    @Insert
    fun addCategory(category:CostCategory):Long

    @Insert
    fun addCategories(categories:List<CostCategory>)

    @Delete
    fun deleteCategory(category: CostCategory)
}