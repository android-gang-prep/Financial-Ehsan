package com.example.financialehsan.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.financialehsan.database.entities.Budget
import com.example.financialehsan.database.entities.Cost
import com.example.financialehsan.database.entities.Revenue
import com.example.financialehsan.database.entities.relations.BudgetWithCategory
import com.example.financialehsan.database.entities.relations.CostWithCategory
import com.example.financialehsan.database.entities.relations.RevenueWithCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {
    @Insert
    fun addBudget(budget: Budget)

    @Update
    fun updateBudget(budget: Budget)

    @Query("SELECT * FROM budgets")
    @Transaction
    fun getBudgets():Flow<List<BudgetWithCategory>>

    @Delete
    fun deleteBudget(budget: Budget)
}