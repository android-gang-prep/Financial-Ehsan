package com.example.financialehsan.repositories

import com.example.financialehsan.database.dao.CostCategoryDao
import com.example.financialehsan.database.dao.RevenueCategoryDao
import com.example.financialehsan.database.entities.CostCategory
import com.example.financialehsan.database.entities.RevenueCategory
import kotlinx.coroutines.flow.Flow

class RevenueCategoryRepository(private val dao: RevenueCategoryDao) {

    fun getCategories():Flow<List<RevenueCategory>>{
        return dao.getCategories()
    }

    fun getLatestCategories():List<RevenueCategory>{
        return dao.getLatestCategories()
    }

    fun addCategory(category:RevenueCategory){
        dao.addCategory(category)
    }

    fun addCategories(categories:List<RevenueCategory>){
        dao.addCategories(categories)
    }

}