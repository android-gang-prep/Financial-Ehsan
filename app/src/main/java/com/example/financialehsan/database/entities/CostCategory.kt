package com.example.financialehsan.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("cost_categories")
data class CostCategory(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("cost_category_id")
    val id:Int =0,
    val title:String
)
