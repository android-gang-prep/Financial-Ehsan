package com.example.financialehsan.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.financialehsan.database.entities.Cost
import com.example.financialehsan.database.entities.Revenue
import com.example.financialehsan.database.entities.relations.CostWithCategory
import com.example.financialehsan.database.entities.relations.RevenueWithCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface RevenueDao {
    @Insert
    fun addRevenue(revenue: Revenue)

    @Update
    fun updateRevenue(revenue: Revenue)

    @Query("SELECT * FROM revenues")
    @Transaction
    fun getRevenues():Flow<List<RevenueWithCategory>>

    @Delete
    fun deleteRevenue(revenue: Revenue)
}