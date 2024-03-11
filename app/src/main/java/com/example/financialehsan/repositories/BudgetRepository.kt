package com.example.financialehsan.repositories

import com.example.financialehsan.database.dao.BudgetDao
import com.example.financialehsan.database.dao.CostDao
import com.example.financialehsan.database.dao.RevenueDao
import com.example.financialehsan.database.entities.Budget
import com.example.financialehsan.database.entities.Cost
import com.example.financialehsan.database.entities.Revenue
import com.example.financialehsan.database.entities.relations.BudgetWithCategory
import com.example.financialehsan.database.entities.relations.CostWithCategory
import com.example.financialehsan.database.entities.relations.RevenueWithCategory
import kotlinx.coroutines.flow.Flow

class BudgetRepository(private val dao:BudgetDao) {
    fun addBudget(budget: Budget){
        dao.addBudget(budget)
    }
    fun updateBudget(budget: Budget){
        dao.updateBudget(budget)
    }
    fun getBudgets():Flow<List<BudgetWithCategory>>{
        return dao.getBudgets()
    }
    fun deleteBudget(budget: Budget){
        return dao.deleteBudget(budget)
    }
}