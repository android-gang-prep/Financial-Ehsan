package com.example.financialehsan.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.financialehsan.database.entities.Cost
import com.example.financialehsan.database.entities.relations.CostWithCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface CostDao {
    @Insert
    fun addCost(cost: Cost)

    @Update
    fun updateCost(cost: Cost)

    @Query("SELECT * FROM costs")
    @Transaction
    fun getCosts():Flow<List<CostWithCategory>>

    @Delete
    fun deleteCost(cost: Cost)
}