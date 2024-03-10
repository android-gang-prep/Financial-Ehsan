package com.example.financialehsan.repositories

import com.example.financialehsan.database.dao.CostDao
import com.example.financialehsan.database.entities.Cost
import com.example.financialehsan.database.entities.relations.CostWithCategory
import kotlinx.coroutines.flow.Flow

class CostRepository(private val dao:CostDao) {
    fun addCost(cost: Cost){
        dao.addCost(cost)
    }
    fun updateCost(cost: Cost){
        dao.updateCost(cost)
    }
    fun getCosts():Flow<List<CostWithCategory>>{
        return dao.getCosts()
    }
    fun deleteCost(cost: Cost){
        return dao.deleteCost(cost)
    }
}