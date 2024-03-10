package com.example.financialehsan.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("revenue_categories")
data class RevenueCategory(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("revenue_category_id")
    val id:Int =0,
    val title:String
)
