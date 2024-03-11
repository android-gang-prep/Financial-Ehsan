package com.example.financialehsan.repositories

import com.example.financialehsan.database.dao.CostDao
import com.example.financialehsan.database.dao.RevenueDao
import com.example.financialehsan.database.entities.Cost
import com.example.financialehsan.database.entities.Revenue
import com.example.financialehsan.database.entities.relations.CostWithCategory
import com.example.financialehsan.database.entities.relations.RevenueWithCategory
import kotlinx.coroutines.flow.Flow

class RevenueRepository(private val dao:RevenueDao) {
    fun addRevenue(revenue: Revenue){
        dao.addRevenue(revenue)
    }
    fun updateRevenue(revenue: Revenue){
        dao.updateRevenue(revenue)
    }
    fun getRevenues():Flow<List<RevenueWithCategory>>{
        return dao.getRevenues()
    }
    fun deleteRevenue(revenue: Revenue){
        return dao.deleteRevenue(revenue)
    }
}