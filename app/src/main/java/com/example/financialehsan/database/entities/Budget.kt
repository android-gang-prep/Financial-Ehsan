package com.example.financialehsan.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity("budgets")
data class Budget(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("budget_id")
    val id:Int = 0,
    @ColumnInfo("budget_category_id")
    val categoryId:Int,
    val amount:Long
)
