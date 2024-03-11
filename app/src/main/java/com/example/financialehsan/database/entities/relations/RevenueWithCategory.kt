package com.example.financialehsan.database.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.financialehsan.database.entities.Cost
import com.example.financialehsan.database.entities.CostCategory
import com.example.financialehsan.database.entities.Revenue
import com.example.financialehsan.database.entities.RevenueCategory

data class RevenueWithCategory (
    @Embedded val revenue: Revenue,
    @Relation(
        entity = RevenueCategory::class,
        entityColumn = "revenue_category_id",
        parentColumn = "category_id"
    )
    val category: RevenueCategory?
)