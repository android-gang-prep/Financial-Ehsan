package com.example.financialehsan.database.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.financialehsan.database.entities.Budget
import com.example.financialehsan.database.entities.CostCategory

data class BudgetWithCategory(
    @Embedded val budget: Budget,
    @Relation(
        entity = CostCategory::class,
        entityColumn = "cost_category_id",
        parentColumn = "budget_category_id"
    )
    val category:CostCategory
)
