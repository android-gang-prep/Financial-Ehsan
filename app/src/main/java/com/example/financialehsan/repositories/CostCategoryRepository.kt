package com.example.financialehsan.repositories

import com.example.financialehsan.database.dao.CostCategoryDao
import com.example.financialehsan.database.entities.CostCategory
import kotlinx.coroutines.flow.Flow

class CostCategoryRepository(private val dao: CostCategoryDao) {

    fun getCategories():Flow<List<CostCategory>>{
        return dao.getCategories()
    }

    fun getLatestCategories():List<CostCategory>{
        return dao.getLatestCategories()
    }

    fun addCategory(category:CostCategory){
        dao.addCategory(category)
    }

    fun addCategories(categories:List<CostCategory>){
        dao.addCategories(categories)
    }

    fun deleteCategory(category: CostCategory){
        dao.deleteCategory(category)
    }

}