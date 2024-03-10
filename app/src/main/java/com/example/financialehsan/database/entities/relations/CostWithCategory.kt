package com.example.financialehsan.database.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.financialehsan.database.entities.Cost
import com.example.financialehsan.database.entities.CostCategory

data class CostWithCategory (
    @Embedded val cost: Cost,
    @Relation(
        entity = CostCategory::class,
        entityColumn = "cost_category_id",
        parentColumn = "category_id"
    )
    val category: CostCategory?
)